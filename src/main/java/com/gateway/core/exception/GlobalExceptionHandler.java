package com.gateway.core.exception;

import com.gateway.core.api.rest.response.ErrorResponse;
import com.gateway.core.monitoring.CorrelationIdFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Global Exception Handler
 * 
 * Centralized exception handling for the entire application.
 * Converts exceptions to standardized error responses.
 * 
 * Never leaks stack traces or internal details to clients (Section 27).
 * 
 * @author Error Handling Team
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePaymentNotFound(PaymentNotFoundException ex) {
        return error(HttpStatus.NOT_FOUND, ErrorCodes.PAYMENT_NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(MerchantNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMerchantNotFound(MerchantNotFoundException ex) {
        return error(HttpStatus.NOT_FOUND, ErrorCodes.MERCHANT_NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(MerchantInactiveException.class)
    public ResponseEntity<ErrorResponse> handleMerchantInactive(MerchantInactiveException ex) {
        return error(HttpStatus.CONFLICT, ErrorCodes.MERCHANT_INACTIVE, ex.getMessage());
    }

    @ExceptionHandler(InvalidPaymentStateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPaymentState(InvalidPaymentStateException ex) {
        return error(HttpStatus.CONFLICT, ErrorCodes.PAYMENT_INVALID_STATE, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Invalid request");
        return error(HttpStatus.BAD_REQUEST, ErrorCodes.INVALID_AMOUNT, message);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return error(HttpStatus.BAD_REQUEST, ErrorCodes.INVALID_AMOUNT, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex) {
        log.error("Unhandled exception", ex);
        return error(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodes.SERVICE_UNAVAILABLE,
                "An unexpected error occurred");
    }

    private ResponseEntity<ErrorResponse> error(HttpStatus status, String code, String message) {
        return ResponseEntity.status(status).body(new ErrorResponse(code, message, currentCorrelationId()));
    }

    private String currentCorrelationId() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        return (String) attributes.getRequest().getAttribute(CorrelationIdFilter.CORRELATION_ID_ATTRIBUTE);
    }
}

