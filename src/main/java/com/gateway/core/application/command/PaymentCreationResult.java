package com.gateway.core.application.command;

import com.gateway.core.domain.model.Payment;

/**
 * Result of handling {@link CreatePaymentCommand}: distinguishes a brand new
 * payment from an idempotent replay of a previous request (Section 6).
 */
public class PaymentCreationResult {

    private final Payment payment;
    private final boolean newlyCreated;

    public PaymentCreationResult(Payment payment, boolean newlyCreated) {
        this.payment = payment;
        this.newlyCreated = newlyCreated;
    }

    public Payment getPayment() {
        return payment;
    }

    public boolean isNewlyCreated() {
        return newlyCreated;
    }
}
