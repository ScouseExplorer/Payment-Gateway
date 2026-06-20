package com.gateway.core.domain.model;

/**
 * PaymentStatus Enum
 * 
 * Represents the current state of a payment in the system.
 * 
 * State Transitions:
 * PENDING -> AUTHORIZED, FAILED
 * AUTHORIZED -> CAPTURED, CANCELLED, FAILED
 * CAPTURED -> SETTLED, REFUNDED
 * SETTLED -> REFUNDED (for partial/full refunds)
 * REFUNDED -> REFUNDED (multiple refunds allowed)
 * CANCELLED -> (terminal state)
 * FAILED -> (terminal state)
 * VOIDED -> (terminal state for reversals)
 * 
 * @author Payment Team
 */
public enum PaymentStatus {
    PENDING("Awaiting authorization from payment provider"),
    AUTHORIZED("Successfully authorized, awaiting capture"),
    CAPTURED("Captured and awaiting settlement"),
    SETTLED("Settled and transferred to merchant"),
    REFUNDED("Refunded to customer"),
    CANCELLED("Payment cancelled"),
    FAILED("Payment authorization failed"),
    VOIDED("Payment voided/reversed");
    
    private String description;
    
    PaymentStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Check if a transition from current state to target state is allowed.
     */
    public boolean canTransitionTo(PaymentStatus target) {
        switch (this) {
            case PENDING:
                return target == AUTHORIZED || target == FAILED;
            case AUTHORIZED:
                return target == CAPTURED || target == CANCELLED || target == FAILED;
            case CAPTURED:
                return target == SETTLED || target == REFUNDED;
            case SETTLED:
                return target == REFUNDED;
            case REFUNDED:
                return target == REFUNDED; // Allow multiple refunds
            case CANCELLED:
            case FAILED:
            case VOIDED:
                return false; // Terminal states
            default:
                return false;
        }
    }
}
