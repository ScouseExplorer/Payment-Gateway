package com.gateway.core.api.rest.response;

import com.gateway.core.domain.model.Payment;

/**
 * API representation of a payment (Section 27: consistent, versioned API shape).
 */
public class PaymentResponse {

    private final String paymentId;
    private final String merchantId;
    private final Long amount;
    private final String currency;
    private final String status;
    private final String paymentMethodType;
    private final String paymentMethodLast4;
    private final String transactionId;
    private final String description;
    private final String orderId;
    private final Long createdAt;
    private final Long updatedAt;

    private PaymentResponse(String paymentId, String merchantId, Long amount, String currency, String status,
                             String paymentMethodType, String paymentMethodLast4, String transactionId,
                             String description, String orderId, Long createdAt, Long updatedAt) {
        this.paymentId = paymentId;
        this.merchantId = merchantId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.paymentMethodType = paymentMethodType;
        this.paymentMethodLast4 = paymentMethodLast4;
        this.transactionId = transactionId;
        this.description = description;
        this.orderId = orderId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getPaymentId().getValue(),
                payment.getMerchant().getMerchantId(),
                payment.getAmount().getAmount(),
                payment.getAmount().getCurrency().name(),
                payment.getStatus().name(),
                payment.getPaymentMethod().getType().name(),
                payment.getPaymentMethod().getLast4Digits(),
                payment.getTransactionId() != null ? payment.getTransactionId().getValue() : null,
                payment.getDescription(),
                payment.getOrderId(),
                payment.getCreatedAt(),
                payment.getUpdatedAt());
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public Long getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getStatus() {
        return status;
    }

    public String getPaymentMethodType() {
        return paymentMethodType;
    }

    public String getPaymentMethodLast4() {
        return paymentMethodLast4;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getDescription() {
        return description;
    }

    public String getOrderId() {
        return orderId;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }
}
