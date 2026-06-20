package com.gateway.core.domain.model;

import java.util.UUID;

/**
 * PaymentId Value Object
 * 
 * Uniquely identifies a payment transaction.
 * Immutable value object ensuring type safety.
 * 
 * @author Payment Team
 */
public class PaymentId {
    private final String value;
    
    private PaymentId(String value) {
        this.value = value;
    }
    
    public static PaymentId of(String value) {
        return new PaymentId(value);
    }
    
    public static PaymentId generate() {
        return new PaymentId(UUID.randomUUID().toString());
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
        if (!(o instanceof PaymentId)) return false;
        PaymentId that = (PaymentId) o;
        return value.equals(that.value);
    }
    
    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
