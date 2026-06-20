package com.gateway.core.domain.event;

/**
 * Domain Event: Payment Refunded
 * 
 * Published when a refund is successfully processed for a payment.
 * May be partial or full refund.
 * 
 * @author Payment Team
 */
public class PaymentRefundedEvent {
    private final String paymentId;
    private final String refundId;
    private final Long refundAmount;
    private final String refundReason;
    private final Long timestamp;
    
    public PaymentRefundedEvent(String paymentId, String refundId, Long refundAmount, String refundReason) {
        this.paymentId = paymentId;
        this.refundId = refundId;
        this.refundAmount = refundAmount;
        this.refundReason = refundReason;
        this.timestamp = System.currentTimeMillis();
    }
    
    // Getters
    public String getPaymentId() { return paymentId; }
    public String getRefundId() { return refundId; }
    public Long getRefundAmount() { return refundAmount; }
    public String getRefundReason() { return refundReason; }
    public Long getTimestamp() { return timestamp; }
}
