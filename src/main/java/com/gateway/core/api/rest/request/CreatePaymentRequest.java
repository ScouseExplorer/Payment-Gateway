package com.gateway.core.api.rest.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

/**
 * Request body for POST /api/payments.
 * The Idempotency-Key is supplied via the {@code Idempotency-Key} header, not the body (Section 6).
 */
public class CreatePaymentRequest {

    @NotBlank
    private String merchantId;

    @NotNull
    @Positive
    private Long amount;

    @NotBlank
    @Pattern(regexp = "[A-Z]{3}", message = "must be a 3-letter ISO 4217 currency code")
    private String currency;

    @NotBlank
    private String paymentMethodType;

    @NotBlank(message = "payment methods must be pre-tokenized; raw card numbers are never accepted")
    private String paymentMethodToken;

    @Pattern(regexp = "\\d{4}", message = "must be exactly 4 digits")
    private String paymentMethodLast4;

    private String description;
    private String orderId;

    @Email
    private String customerEmail;

    private String customerIp;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPaymentMethodType() {
        return paymentMethodType;
    }

    public void setPaymentMethodType(String paymentMethodType) {
        this.paymentMethodType = paymentMethodType;
    }

    public String getPaymentMethodToken() {
        return paymentMethodToken;
    }

    public void setPaymentMethodToken(String paymentMethodToken) {
        this.paymentMethodToken = paymentMethodToken;
    }

    public String getPaymentMethodLast4() {
        return paymentMethodLast4;
    }

    public void setPaymentMethodLast4(String paymentMethodLast4) {
        this.paymentMethodLast4 = paymentMethodLast4;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerIp() {
        return customerIp;
    }

    public void setCustomerIp(String customerIp) {
        this.customerIp = customerIp;
    }
}
