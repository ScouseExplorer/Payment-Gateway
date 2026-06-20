package com.gateway.core.domain.event;

/**
 * Domain Event: Payment Authorized
 * 
 * Published when a payment is successfully authorized by the payment provider.
 * Indicates funds are reserved but not yet captured.
 * 
 * @author Payment Team
 */
public class PaymentAuthorisedEvent {
    private String paymentId;
    private String authorizationCode;
    private Long authorizedAmount;
    private String authorizationProvider;
    private Long timestamp;
    
    public PaymentAuthorisedEvent(String paymentId, String authorizationCode, 
                                  Long authorizedAmount, String authorizationProvider) {
        this.paymentId = paymentId;
        this.authorizationCode = authorizationCode;
        this.authorizedAmount = authorizedAmount;
        this.authorizationProvider = authorizationProvider;
        this.timestamp = System.currentTimeMillis();
    }
    
    // Getters
    public String getPaymentId() { return paymentId; }
    public String getAuthorizationCode() { return authorizationCode; }
    public Long getAuthorizedAmount() { return authorizedAmount; }
    public String getAuthorizationProvider() { return authorizationProvider; }
    public Long getTimestamp() { return timestamp; }
}
