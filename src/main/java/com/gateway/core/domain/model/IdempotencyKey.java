package com.gateway.core.domain.model;

/**
 * IdempotencyKey Value Object
 * 
 * Prevents duplicate payments by ensuring the same key results in same response.
 * Immutable value object for idempotent request handling.
 * 
 * @author Payment Team
 */
public class IdempotencyKey {
    private final String value;
    
    private IdempotencyKey(String value) {
        this.value = value;
    }
    
    public static IdempotencyKey of(String value) {
        return new IdempotencyKey(value);
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
        if (!(o instanceof IdempotencyKey)) return false;
        IdempotencyKey that = (IdempotencyKey) o;
        return value.equals(that.value);
    }
    
    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
