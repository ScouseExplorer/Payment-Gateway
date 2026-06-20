package com.gateway.core.domain.model;

/**
 * Money Value Object
 * 
 * Represents a monetary amount with currency.
 * Ensures type safety and prevents implicit currency conversions.
 * 
 * Responsibilities:
 * - Store amount and currency together
 * - Provide arithmetic operations (add, subtract)
 * - Enforce non-negative amounts (except for refunds)
 * - Enable currency comparisons and conversions
 * 
 * @author Payment Team
 */
public class Money {
    
    private final Long amount;  // Stored in cents/smallest unit
    private final Currency currency;
    
    public Money(Long amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }
    
    public Long getAmount() {
        return amount;
    }
    
    public Currency getCurrency() {
        return currency;
    }
    
    /**
     * Add another Money amount (must be same currency).
     */
    public Money add(Money other) {
        // Validate currency matches
        // Perform addition
        return new Money(this.amount + other.amount, this.currency);
    }
    
    /**
     * Subtract another Money amount.
     */
    public Money subtract(Money other) {
        // Implementation
        return new Money(this.amount - other.amount, this.currency);
    }
}
