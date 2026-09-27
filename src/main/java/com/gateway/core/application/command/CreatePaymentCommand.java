package com.gateway.core.application.command;

/**
 * Command: Create Payment
 * 
 * Initiates a new payment transaction.
 * CQRS command that encapsulates all necessary data to create a payment.
 * 
 * SECURITY: this intentionally has no raw card number field. Card data must
 * already be tokenized (e.g. by a client-side SDK or PCI-compliant vault)
 * before it reaches this service (Section 19: never store raw card numbers).
 * 
 * @author Payment Team
 */
public class CreatePaymentCommand {
    private final String idempotencyKey;
    private final String merchantId;
    private final Long amount;
    private final String currency;
    private final String paymentMethodType;
    private String paymentMethodToken;
    private String paymentMethodLast4;
    private String customerEmail;
    private String customerIp;
    private String description;
    private String orderId;
    
    public CreatePaymentCommand(String idempotencyKey, String merchantId, Long amount, 
                                String currency, String paymentMethodType) {
        this.idempotencyKey = idempotencyKey;
        this.merchantId = merchantId;
        this.amount = amount;
        this.currency = currency;
        this.paymentMethodType = paymentMethodType;
    }
    
    // Getters and setters
    public String getIdempotencyKey() { return idempotencyKey; }
    public String getMerchantId() { return merchantId; }
    public Long getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getPaymentMethodType() { return paymentMethodType; }
    public String getPaymentMethodToken() { return paymentMethodToken; }
    public String getPaymentMethodLast4() { return paymentMethodLast4; }
    public String getCustomerEmail() { return customerEmail; }
    public String getCustomerIp() { return customerIp; }
    public String getDescription() { return description; }
    public String getOrderId() { return orderId; }
    
    public void setPaymentMethodToken(String paymentMethodToken) { this.paymentMethodToken = paymentMethodToken; }
    public void setPaymentMethodLast4(String paymentMethodLast4) { this.paymentMethodLast4 = paymentMethodLast4; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public void setCustomerIp(String customerIp) { this.customerIp = customerIp; }
    public void setDescription(String description) { this.description = description; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
}

