package com.example.Auth.security;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.Auth.api.response.GenericResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final class WindowCounter {
        private long windowStartMillis;
        private int count;

        WindowCounter(final long windowStartMillis) {
            this.windowStartMillis = windowStartMillis;
            this.count = 0;
        }
    }

    private final ObjectMapper objectMapper;
    private final int maxRequests;
    private final long windowMillis;
    private final ConcurrentMap<String, WindowCounter> counters = new ConcurrentHashMap<>();

    public RateLimitFilter(
            final ObjectMapper objectMapper,
            @Value("${rate.limit.requests:60}") final int maxRequests,
            @Value("${rate.limit.window-seconds:60}") final int windowSeconds
    ) {
        this.objectMapper = objectMapper;
        this.maxRequests = Math.max(1, maxRequests);
        this.windowMillis = Math.max(1, windowSeconds) * 1000L;
    }

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain
    ) throws ServletException, IOException {
        final String key = buildKey(request);
        if (isRateLimited(key)) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(
                    GenericResponse.failure("Rate limit exceeded. Please try again later.")
            ));
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isRateLimited(final String key) {
        final long now = Instant.now().toEpochMilli();
        final WindowCounter counter = counters.computeIfAbsent(key, k -> new WindowCounter(now));
        synchronized (counter) {
            if (now - counter.windowStartMillis >= windowMillis) {
                counter.windowStartMillis = now;
                counter.count = 0;
            }
            counter.count++;
            return counter.count > maxRequests;
        }
    }

    private String buildKey(final HttpServletRequest request) {
        final String ip = getClientIp(request);
        final String path = request.getRequestURI();
        final String method = request.getMethod();
        return ip + "|" + method + "|" + path;
    }

    private String getClientIp(final HttpServletRequest request) {
        final String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            final String first = forwarded.split(",")[0].trim();
            if (!first.isBlank()) {
                return first;
            }
        }
        return request.getRemoteAddr();
    }
}
