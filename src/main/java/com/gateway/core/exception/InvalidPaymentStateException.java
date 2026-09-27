package com.gateway.core.exception;

import com.gateway.core.domain.model.PaymentStatus;

/**
 * Thrown when a payment state transition is not permitted by the payment
 * state machine (see {@link PaymentStatus#canTransitionTo}).
 */
public class InvalidPaymentStateException extends RuntimeException {

    public InvalidPaymentStateException(String paymentId, PaymentStatus from, PaymentStatus to) {
        super("Payment " + paymentId + " cannot transition from " + from + " to " + to);
    }
}
