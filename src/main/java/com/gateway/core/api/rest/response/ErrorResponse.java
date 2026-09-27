package com.gateway.core.api.rest.response;

import java.time.Instant;

/**
 * Standardized API error body (see Section 27 of the project instructions).
 */
public class ErrorResponse {

    private final String code;
    private final String message;
    private final Instant timestamp;
    private final String correlationId;

    public ErrorResponse(String code, String message, String correlationId) {
        this.code = code;
        this.message = message;
        this.correlationId = correlationId;
        this.timestamp = Instant.now();
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getCorrelationId() {
        return correlationId;
    }
}
