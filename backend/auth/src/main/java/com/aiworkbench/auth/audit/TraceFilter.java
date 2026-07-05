package com.aiworkbench.auth.audit;

import java.io.IOException;
import java.util.UUID;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TraceFilter extends OncePerRequestFilter {

    private static final String TRACE_ID_HEADER = "X-Trace-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final long start = System.nanoTime();

        String incomingTraceId = request.getHeader(TRACE_ID_HEADER);

        final String traceId = (incomingTraceId == null || incomingTraceId.isBlank())
                ? UUID.randomUUID().toString()
                : incomingTraceId;

        response.setHeader(TRACE_ID_HEADER, traceId);

        log.info("Incoming {} {} traceId={}",
                request.getMethod(),
                request.getRequestURI(),
                traceId);

        try {
            filterChain.doFilter(request, response);
        } finally {

            long duration = (System.nanoTime() - start) / 1_000_000;

            log.info("Completed {} in {} ms traceId={}",
                    response.getStatus(),
                    duration,
                    traceId);
        }
    }
}