package com.gateway.core.domain.model;

/**
 * Currency Enum
 * 
 * Supported currencies for payment processing.
 * Each currency includes ISO 4217 code and decimal places for rounding.
 * 
 * @author Payment Team
 */
public enum Currency {
    USD("US Dollar", 2),
    EUR("Euro", 2),
    GBP("British Pound", 2),
    JPY("Japanese Yen", 0),
    CNY("Chinese Yuan", 2),
    INR("Indian Rupee", 2),
    AUD("Australian Dollar", 2),
    CAD("Canadian Dollar", 2),
    SGD("Singapore Dollar", 2),
    HKD("Hong Kong Dollar", 2);
    
    private final String displayName;
    private final int decimalPlaces;
    
    Currency(String displayName, int decimalPlaces) {
        this.displayName = displayName;
        this.decimalPlaces = decimalPlaces;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public int getDecimalPlaces() {
        return decimalPlaces;
    }
}
