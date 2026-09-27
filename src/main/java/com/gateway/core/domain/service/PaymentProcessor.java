package com.gateway.core.domain.service;

import com.gateway.core.domain.model.Payment;
import com.gateway.core.domain.model.RiskScore;
import com.gateway.core.domain.model.TransactionId;
import org.springframework.stereotype.Service;

/**
 * Payment Processor
 * 
 * Core domain service orchestrating payment processing workflow.
 * 
 * NOTE: Stage 1 has no real acquiring bank / card network integration yet
 * (see Section 29, Stage 6). Authorization is currently simulated so the
 * payment state machine and idempotency guarantees can be built and tested
 * end-to-end. Replace {@link #authorize} with a real provider call behind
 * a provider abstraction once external processors are integrated.
 * 
 * @author Domain Team
 */
@Service
public class PaymentProcessor {

    /**
     * Simulate authorization of a pending payment and apply the result to it.
     * Always approves for now; there is no fraud/risk engine wired in yet.
     */
    public void authorize(Payment payment) {
        RiskScore riskScore = RiskScore.of(0);
        TransactionId transactionId = TransactionId.of("sim_" + payment.getPaymentId().getValue());
        payment.authorize(transactionId, riskScore);
    }

    /**
     * Capture previously authorized payment.
     */
    public void capturePayment(Payment payment) {
        payment.capture();
    }

    /**
     * Cancel or void a payment.
     */
    public void cancelPayment(Payment payment) {
        payment.cancel();
    }
}

