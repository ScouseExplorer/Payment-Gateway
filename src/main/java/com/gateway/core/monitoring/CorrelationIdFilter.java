package com.gateway.core.monitoring;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Correlation ID Filter
 * 
 * Generates and propagates correlation IDs across all requests and services.
 * Enables end-to-end request tracing.
 * 
 * @author Operations Team
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorrelationIdFilter extends OncePerRequestFilter {

    public static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    public static final String CORRELATION_ID_ATTRIBUTE = "correlationId";
    private static final String MDC_KEY = "correlationId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String correlationId = getOrGenerateCorrelationId(request.getHeader(CORRELATION_ID_HEADER));
        request.setAttribute(CORRELATION_ID_ATTRIBUTE, correlationId);
        response.setHeader(CORRELATION_ID_HEADER, correlationId);
        MDC.put(MDC_KEY, correlationId);
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove(MDC_KEY);
        }
    }

    /**
     * Generate or retrieve correlation ID.
     */
    public String getOrGenerateCorrelationId(String incomingId) {
        if (incomingId != null && !incomingId.isBlank()) {
            return incomingId;
        }
        return UUID.randomUUID().toString();
    }
}

