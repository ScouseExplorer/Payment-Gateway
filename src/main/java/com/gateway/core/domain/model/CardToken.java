package com.gateway.core.domain.model;

/**
 * CardToken Value Object
 * 
 * Represents a tokenized card for secure payment processing.
 * Never stores actual card numbers - only tokens and masked digits.
 * 
 * @author Payment Team
 */
public class CardToken {
    private final String token;
    private final String last4Digits;
    private final String cardBrand;
    private final String expiryMonth;
    private final String expiryYear;
    
    public CardToken(String token, String last4Digits, String cardBrand, 
                     String expiryMonth, String expiryYear) {
        this.token = token;
        this.last4Digits = last4Digits;
        this.cardBrand = cardBrand;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
    }
    
    public String getToken() {
        return token;
    }
    
    public String getLast4Digits() {
        return last4Digits;
    }
    
    public String getCardBrand() {
        return cardBrand;
    }
    
    public String getExpiryMonth() {
        return expiryMonth;
    }
    
    public String getExpiryYear() {
        return expiryYear;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CardToken)) return false;
        CardToken that = (CardToken) o;
        return token.equals(that.token);
    }
    
    @Override
    public int hashCode() {
        return token.hashCode();
    }
}
