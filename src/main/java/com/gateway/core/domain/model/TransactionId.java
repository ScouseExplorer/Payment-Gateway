package com.gateway.core.domain.model;

/**
 * TransactionId Value Object
 * 
 * Uniquely identifies a transaction at the payment provider level.
 * Different from PaymentId - this is assigned by the external gateway.
 * 
 * @author Payment Team
 */
public class TransactionId {
    private final String value;
    
    private TransactionId(String value) {
        this.value = value;
    }
    
    public static TransactionId of(String value) {
        return new TransactionId(value);
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransactionId)) return false;
        TransactionId that = (TransactionId) o;
        return value.equals(that.value);
    }
    
    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
