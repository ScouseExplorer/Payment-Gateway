package com.gateway.core.domain.model;

import com.gateway.core.exception.InvalidPaymentStateException;

/**
 * Payment Domain Model
 * 
 * Core aggregate root representing a payment transaction.
 * 
 * Responsibilities:
 * - Maintains payment lifecycle (created -> authorized -> captured/cancelled -> settled)
 * - Enforces business rules for payment operations
 * - Publishes domain events for async processing
 * - Tracks payment metadata and audit information
 * 
 * Payment States:
 * - PENDING: Awaiting authorization from payment provider
 * - AUTHORIZED: Successfully authorized, awaiting capture
 * - CAPTURED: Funds captured, awaiting settlement
 * - SETTLED: Funds transferred to merchant
 * - REFUNDED: Partially or fully refunded
 * - CANCELLED: Transaction cancelled
 * - FAILED: Payment authorization failed
 * 
 * Payment can be for different methods:
 * - Credit/Debit Cards (Visa, Mastercard, Amex)
 * - Digital Wallets (Apple Pay, Google Pay)
 * - Bank Transfers
 * - Alternative Payment Methods
 * 
 * @author Payment Team
 */
public class Payment {
    
    // Payment identifier
    private final PaymentId paymentId;
    
    // Idempotency key to prevent duplicate payments
    private final IdempotencyKey idempotencyKey;
    
    // Merchant information
    private final Merchant merchant;
    
    // Payment amount and currency
    private final Money amount;
    
    // Payment method details
    private final PaymentMethod paymentMethod;
    
    // Current payment status
    private PaymentStatus status;
    
    // Transaction ID from payment provider
    private TransactionId transactionId;
    
    // Fraud risk score (0-100)
    private RiskScore riskScore;
    
    // Additional metadata
    private final String description;
    private final String orderId;
    private final String customerEmail;
    private final String customerIp;
    
    // Audit information
    private final Long createdAt;
    private Long updatedAt;
    
    public Payment(PaymentId paymentId, IdempotencyKey idempotencyKey, Merchant merchant, 
                   Money amount, PaymentMethod paymentMethod, String description, 
                   String orderId, String customerEmail, String customerIp) {
        this.paymentId = paymentId;
        this.idempotencyKey = idempotencyKey;
        this.merchant = merchant;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.description = description;
        this.orderId = orderId;
        this.customerEmail = customerEmail;
        this.customerIp = customerIp;
        this.createdAt = System.currentTimeMillis();
        this.status = PaymentStatus.PENDING;
    }

    /**
     * Reconstructs a payment from persisted state. Bypasses the state machine
     * since the transitions have already been validated at the time they occurred.
     */
    public static Payment reconstruct(PaymentId paymentId, IdempotencyKey idempotencyKey, Merchant merchant,
                                       Money amount, PaymentMethod paymentMethod, PaymentStatus status,
                                       TransactionId transactionId, RiskScore riskScore, String description,
                                       String orderId, String customerEmail, String customerIp,
                                       Long createdAt, Long updatedAt) {
        Payment payment = new Payment(paymentId, idempotencyKey, merchant, amount, paymentMethod,
                description, orderId, customerEmail, customerIp);
        payment.status = status;
        payment.transactionId = transactionId;
        payment.riskScore = riskScore;
        payment.updatedAt = updatedAt;
        return payment;
    }
    
    /**
     * Authorize a payment following a (simulated) provider authorization result.
     */
    public void authorize(TransactionId transactionId, RiskScore riskScore) {
        transitionTo(PaymentStatus.AUTHORIZED);
        this.transactionId = transactionId;
        this.riskScore = riskScore;
    }

    /**
     * Mark a payment as failed, e.g. when provider authorization is declined.
     */
    public void markFailed() {
        transitionTo(PaymentStatus.FAILED);
    }
    
    /**
     * Capture previously authorized funds.
     */
    public void capture() {
        transitionTo(PaymentStatus.CAPTURED);
    }
    
    /**
     * Cancel a pending or authorized payment.
     */
    public void cancel() {
        transitionTo(PaymentStatus.CANCELLED);
    }
    
    /**
     * Void (reverse) an authorized transaction.
     * Not yet reachable: {@link PaymentStatus#canTransitionTo} has no path to VOIDED
     * until the reversal business rules are defined.
     */
    public void voidPayment() {
        transitionTo(PaymentStatus.VOIDED);
    }

    private void transitionTo(PaymentStatus target) {
        if (!status.canTransitionTo(target)) {
            throw new InvalidPaymentStateException(paymentId.getValue(), status, target);
        }
        this.status = target;
        this.updatedAt = System.currentTimeMillis();
    }
    
    // Getters
    public PaymentId getPaymentId() {
        return paymentId;
    }
    
    public IdempotencyKey getIdempotencyKey() {
        return idempotencyKey;
    }
    
    public Merchant getMerchant() {
        return merchant;
    }
    
    public Money getAmount() {
        return amount;
    }
    
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    
    public PaymentStatus getStatus() {
        return status;
    }
    
    public TransactionId getTransactionId() {
        return transactionId;
    }
    
    public RiskScore getRiskScore() {
        return riskScore;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getOrderId() {
        return orderId;
    }
    
    public String getCustomerEmail() {
        return customerEmail;
    }
    
    public String getCustomerIp() {
        return customerIp;
    }
    
    public Long getCreatedAt() {
        return createdAt;
    }
    
    public Long getUpdatedAt() {
        return updatedAt;
    }
}
