package com.gateway.core.application.command;

/**
 * Command: Create Payment
 * 
 * Initiates a new payment transaction.
 * CQRS command that encapsulates all necessary data to create a payment.
 * 
 * @author Payment Team
 */
public class CreatePaymentCommand {
    private final String idempotencyKey;
    private final String merchantId;
    private final Long amount;
    private final String currency;
    private final String paymentMethodType;
    private String cardNumber;
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
    public String getCardNumber() { return cardNumber; }
    public String getCustomerEmail() { return customerEmail; }
    public String getCustomerIp() { return customerIp; }
    public String getDescription() { return description; }
    public String getOrderId() { return orderId; }
    
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public void setCustomerIp(String customerIp) { this.customerIp = customerIp; }
    public void setDescription(String description) { this.description = description; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
}
