package com.gateway.core.domain.event;

/**
 * Domain Event: Payment Captured
 * 
 * Published when funds are successfully captured for an authorized payment.
 * Triggers settlement processing.
 * 
 * @author Payment Team
 */
public class PaymentCapturedEvent {
    private String paymentId;
    private String transactionId;
    private Long capturedAmount;
    private Long timestamp;
    
    public PaymentCapturedEvent(String paymentId, String transactionId, Long capturedAmount) {
        this.paymentId = paymentId;
        this.transactionId = transactionId;
        this.capturedAmount = capturedAmount;
        this.timestamp = System.currentTimeMillis();
    }
    
    // Getters
    public String getPaymentId() { return paymentId; }
    public String getTransactionId() { return transactionId; }
    public Long getCapturedAmount() { return capturedAmount; }
    public Long getTimestamp() { return timestamp; }
}
