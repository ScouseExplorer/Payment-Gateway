package com.gateway.core.application.command;

/**
 * Command: Refund Payment
 * 
 * Initiates a refund for a captured payment.
 * CQRS command for processing refunds.
 * 
 * @author Payment Team
 */
public class RefundPaymentCommand {
    private final String paymentId;
    private final Long refundAmount;
    private final String reason;
    private String initiatedBy;
    
    public RefundPaymentCommand(String paymentId, Long refundAmount, String reason) {
        this.paymentId = paymentId;
        this.refundAmount = refundAmount;
        this.reason = reason;
    }
    
    // Getters and setters
    public String getPaymentId() { return paymentId; }
    public Long getRefundAmount() { return refundAmount; }
    public String getReason() { return reason; }
    public String getInitiatedBy() { return initiatedBy; }
    public void setInitiatedBy(String initiatedBy) { this.initiatedBy = initiatedBy; }
}
