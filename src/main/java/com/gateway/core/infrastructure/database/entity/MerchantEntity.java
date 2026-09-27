package com.gateway.core.infrastructure.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * JPA persistence model for {@link com.gateway.core.domain.model.Merchant}.
 */
@Entity
@Table(name = "merchants")
public class MerchantEntity {

    @Id
    @Column(name = "merchant_id", length = 64, nullable = false)
    private String merchantId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "mcc", length = 10)
    private String mcc;

    @Column(name = "bank_account")
    private String bankAccount;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected MerchantEntity() {
        // required by JPA
    }

    public MerchantEntity(String merchantId, String name, String mcc, String bankAccount, boolean active,
                          Instant createdAt) {
        this.merchantId = merchantId;
        this.name = name;
        this.mcc = mcc;
        this.bankAccount = bankAccount;
        this.active = active;
        this.createdAt = createdAt;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public String getName() {
        return name;
    }

    public String getMcc() {
        return mcc;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
