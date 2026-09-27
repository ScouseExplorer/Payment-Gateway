package com.gateway.core.exception;

/**
 * Thrown when a payment is attempted for a merchant that is not active.
 */
public class MerchantInactiveException extends RuntimeException {

    public MerchantInactiveException(String merchantId) {
        super("Merchant is not active: " + merchantId);
    }
}
