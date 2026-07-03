package com.aiworkbench.auth.audit;

import java.io.IOException;
import java.util.UUID;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TraceFilter extends OncePerRequestFilter {

    private static final String TRACE_ID_HEADER = "X-Trace-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String incomingTraceId = request.getHeader(TRACE_ID_HEADER);

        final String traceId = (incomingTraceId == null || incomingTraceId.isBlank())
                ? UUID.randomUUID().toString()
                : incomingTraceId;

        // Store traceId in MDC so it appears in every log
        MDC.put("traceId", traceId);

        // Return the trace ID to the caller
        response.setHeader(TRACE_ID_HEADER, traceId);

        long start = System.currentTimeMillis();

        try {
            log.info("➡️ Incoming Request | method={} | uri={}",
                    request.getMethod(),
                    request.getRequestURI());

            filterChain.doFilter(request, response);

            long duration = System.currentTimeMillis() - start;

            log.info("✅ Request Completed | status={} | timeMs={}",
                    response.getStatus(),
                    duration);

        } catch (Exception ex) {

            log.error("❌ Request Failed | status={} | error={}",
                    response.getStatus(),
                    ex.getMessage(),
                    ex);

            throw ex;

        } finally {
            MDC.clear();
        }
    }
}