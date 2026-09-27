package com.gateway.core.infrastructure.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;

import com.gateway.core.domain.model.Currency;
import com.gateway.core.domain.model.PaymentMethodType;
import com.gateway.core.domain.model.PaymentStatus;

/**
 * JPA persistence model for {@link com.gateway.core.domain.model.Payment}.
 * The unique constraint on (merchant_id, idempotency_key) is the source of
 * truth that enforces idempotent payment creation (Section 6).
 */
@Entity
@Table(name = "payments", uniqueConstraints =
        @UniqueConstraint(name = "uq_payments_merchant_idempotency", columnNames = {"merchant_id", "idempotency_key"}))
public class PaymentEntity {

    @Id
    @Column(name = "payment_id", length = 64, nullable = false)
    private String paymentId;

    @Column(name = "idempotency_key", nullable = false)
    private String idempotencyKey;

    @Column(name = "merchant_id", length = 64, nullable = false)
    private String merchantId;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", length = 3, nullable = false)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method_type", length = 30, nullable = false)
    private PaymentMethodType paymentMethodType;

    @Column(name = "payment_method_token")
    private String paymentMethodToken;

    @Column(name = "payment_method_last4", length = 4)
    private String paymentMethodLast4;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private PaymentStatus status;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "risk_score")
    private Integer riskScore;

    @Column(name = "description")
    private String description;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "customer_ip")
    private String customerIp;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    protected PaymentEntity() {
        // required by JPA
    }

    public PaymentEntity(String paymentId, String idempotencyKey, String merchantId, Long amount,
                          Currency currency, PaymentMethodType paymentMethodType, String paymentMethodToken,
                          String paymentMethodLast4, PaymentStatus status, String transactionId,
                          Integer riskScore, String description, String orderId, String customerEmail,
                          String customerIp, Instant createdAt, Instant updatedAt) {
        this.paymentId = paymentId;
        this.idempotencyKey = idempotencyKey;
        this.merchantId = merchantId;
        this.amount = amount;
        this.currency = currency;
        this.paymentMethodType = paymentMethodType;
        this.paymentMethodToken = paymentMethodToken;
        this.paymentMethodLast4 = paymentMethodLast4;
        this.status = status;
        this.transactionId = transactionId;
        this.riskScore = riskScore;
        this.description = description;
        this.orderId = orderId;
        this.customerEmail = customerEmail;
        this.customerIp = customerIp;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public Long getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public PaymentMethodType getPaymentMethodType() {
        return paymentMethodType;
    }

    public String getPaymentMethodToken() {
        return paymentMethodToken;
    }

    public String getPaymentMethodLast4() {
        return paymentMethodLast4;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(Integer riskScore) {
        this.riskScore = riskScore;
    }

    public String getDescription() {
        return description;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getCustomerIp() {
        return customerIp;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
