package com.gateway.core.domain.model;

/**
 * PaymentMethod Value Object
 * 
 * Represents the method used for payment (card, wallet, bank transfer, etc.)
 * Contains payment method type and token/reference.
 * 
 * @author Payment Team
 */
public class PaymentMethod {
    private final PaymentMethodType type;
    private final String token;  // Tokenized card, wallet ID, or bank account reference
    private final String last4Digits;  // Last 4 digits for display
    
    public PaymentMethod(PaymentMethodType type, String token, String last4Digits) {
        this.type = type;
        this.token = token;
        this.last4Digits = last4Digits;
    }
    
    public PaymentMethodType getType() {
        return type;
    }
    
    public String getToken() {
        return token;
    }
    
    public String getLast4Digits() {
        return last4Digits;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentMethod)) return false;
        PaymentMethod that = (PaymentMethod) o;
        return type == that.type && token.equals(that.token);
    }
    
    @Override
    public int hashCode() {
        return type.hashCode() * 31 + token.hashCode();
    }
}
