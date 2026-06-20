package com.gateway.core.application.command;

/**
 * Command: Capture Payment
 * 
 * Captures previously authorized funds.
 * CQRS command for capturing an authorized payment.
 * 
 * @author Payment Team
 */
public class CapturePaymentCommand {
    private final String paymentId;
    private final Long captureAmount;
    private String reason;
    
    public CapturePaymentCommand(String paymentId, Long captureAmount) {
        this.paymentId = paymentId;
        this.captureAmount = captureAmount;
    }
    
    // Getters and setters
    public String getPaymentId() { return paymentId; }
    public Long getCaptureAmount() { return captureAmount; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
