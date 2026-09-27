package com.gateway.core.application.command;

/**
 * Command: Cancel Payment
 * 
 * Cancels a pending or authorized payment before capture.
 * 
 * @author Payment Team
 */
public class CancelPaymentCommand {
    private final String paymentId;
    private final String reason;

    public CancelPaymentCommand(String paymentId, String reason) {
        this.paymentId = paymentId;
        this.reason = reason;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getReason() {
        return reason;
    }
}
