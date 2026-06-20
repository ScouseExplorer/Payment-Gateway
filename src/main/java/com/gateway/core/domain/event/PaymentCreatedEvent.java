package com.gateway.core.domain.event;

/**
 * Domain Event: Payment Created
 * 
 * Published when a new payment is created in the system.
 * Triggers fraud analysis and risk scoring.
 * 
 * @author Payment Team
 */
public class PaymentCreatedEvent {
    private String paymentId;
    private String merchantId;
    private Long amount;
    private String currency;
    private String paymentMethod;
    private Long timestamp;
    
    public PaymentCreatedEvent(String paymentId, String merchantId, Long amount, 
                               String currency, String paymentMethod) {
        this.paymentId = paymentId;
        this.merchantId = merchantId;
        this.amount = amount;
        this.currency = currency;
        this.paymentMethod = paymentMethod;
        this.timestamp = System.currentTimeMillis();
    }
    
    // Getters
    public String getPaymentId() { return paymentId; }
    public String getMerchantId() { return merchantId; }
    public Long getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getPaymentMethod() { return paymentMethod; }
    public Long getTimestamp() { return timestamp; }
}
