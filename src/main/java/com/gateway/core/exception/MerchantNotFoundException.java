package com.gateway.core.exception;

/**
 * Thrown when a merchant cannot be found by its identifier.
 */
public class MerchantNotFoundException extends RuntimeException {

    public MerchantNotFoundException(String merchantId) {
        super("Merchant not found: " + merchantId);
    }
}
