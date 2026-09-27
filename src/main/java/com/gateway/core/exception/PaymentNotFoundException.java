package com.gateway.core.exception;

/**
 * Thrown when a payment cannot be found by its identifier.
 */
public class PaymentNotFoundException extends RuntimeException {

    public PaymentNotFoundException(String paymentId) {
        super("Payment not found: " + paymentId);
    }
}
