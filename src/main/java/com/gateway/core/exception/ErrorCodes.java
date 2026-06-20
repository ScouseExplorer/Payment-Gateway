package com.gateway.core.exception;

/**
 * Error Codes
 * 
 * Standardized error codes for the payment core service.
 * Used in API error responses and logging.
 * 
 * @author Error Handling Team
 */
public class ErrorCodes {
    
    // Payment Errors (4000-4999)
    public static final String PAYMENT_NOT_FOUND = "PAYMENT_NOT_FOUND";
    public static final String PAYMENT_INVALID_STATE = "PAYMENT_INVALID_STATE";
    public static final String PAYMENT_PROCESSING_FAILED = "PAYMENT_PROCESSING_FAILED";
    public static final String PAYMENT_TIMEOUT = "PAYMENT_TIMEOUT";
    
    // Validation Errors (4100-4199)
    public static final String INVALID_AMOUNT = "INVALID_AMOUNT";
    public static final String INVALID_CURRENCY = "INVALID_CURRENCY";
    public static final String INVALID_CARD_NUMBER = "INVALID_CARD_NUMBER";
    public static final String DUPLICATE_PAYMENT = "DUPLICATE_PAYMENT";
    
    // Fraud Errors (4200-4299)
    public static final String FRAUD_DETECTED = "FRAUD_DETECTED";
    public static final String BLOCKED_CARD = "BLOCKED_CARD";
    public static final String VELOCITY_EXCEEDED = "VELOCITY_EXCEEDED";
    
    // Authorization Errors (4300-4399)
    public static final String UNAUTHORIZED = "UNAUTHORIZED";
    public static final String INVALID_API_KEY = "INVALID_API_KEY";
    public static final String PERMISSION_DENIED = "PERMISSION_DENIED";
    
    // Merchant Errors (4400-4499)
    public static final String MERCHANT_NOT_FOUND = "MERCHANT_NOT_FOUND";
    public static final String MERCHANT_INACTIVE = "MERCHANT_INACTIVE";
    
    // External Service Errors (5000-5999)
    public static final String GATEWAY_UNAVAILABLE = "GATEWAY_UNAVAILABLE";
    public static final String DATABASE_ERROR = "DATABASE_ERROR";
    public static final String SERVICE_UNAVAILABLE = "SERVICE_UNAVAILABLE";
}
