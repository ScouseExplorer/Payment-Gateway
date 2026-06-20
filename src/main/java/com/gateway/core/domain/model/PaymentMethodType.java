package com.gateway.core.domain.model;

/**
 * PaymentMethodType Enum
 * 
 * Supported payment method types for transactions.
 * 
 * @author Payment Team
 */
public enum PaymentMethodType {
    CARD("Credit/Debit Card"),
    WALLET("Digital Wallet"),
    BANK_TRANSFER("Bank Transfer"),
    ACH("ACH Transfer"),
    ALTERNATIVE("Alternative Payment Method");
    
    private final String description;
    
    PaymentMethodType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
