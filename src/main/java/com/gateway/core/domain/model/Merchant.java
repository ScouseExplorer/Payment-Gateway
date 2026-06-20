package com.gateway.core.domain.model;

/**
 * Merchant Entity
 * 
 * Represents a merchant account in the payment system.
 * Contains merchant identification and configuration.
 * 
 * @author Payment Team
 */
public class Merchant {
    private final String merchantId;
    private final String name;
    private final String mcc;  // Merchant Category Code
    private final String bankAccount;
    private final boolean active;
    
    public Merchant(String merchantId, String name, String mcc, String bankAccount, boolean active) {
        this.merchantId = merchantId;
        this.name = name;
        this.mcc = mcc;
        this.bankAccount = bankAccount;
        this.active = active;
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Merchant)) return false;
        Merchant merchant = (Merchant) o;
        return merchantId.equals(merchant.merchantId);
    }
    
    @Override
    public int hashCode() {
        return merchantId.hashCode();
    }
}
