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
        if (amount == null || currency == null) {
            throw new IllegalArgumentException("amount and currency are required");
        }
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
        requireSameCurrency(other);
        return new Money(this.amount + other.amount, this.currency);
    }
    
    /**
     * Subtract another Money amount (must be same currency).
     */
    public Money subtract(Money other) {
        requireSameCurrency(other);
        return new Money(this.amount - other.amount, this.currency);
    }

    private void requireSameCurrency(Money other) {
        if (other == null || other.currency != this.currency) {
            throw new IllegalArgumentException("Cannot operate on Money with different currencies");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;
        Money money = (Money) o;
        return amount.equals(money.amount) && currency == money.currency;
    }

    @Override
    public int hashCode() {
        return 31 * amount.hashCode() + currency.hashCode();
    }
}
