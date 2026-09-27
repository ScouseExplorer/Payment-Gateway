package com.gateway.core.domain.model;

import com.gateway.core.exception.InvalidPaymentStateException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PaymentTest {

    private Payment newPayment() {
        Merchant merchant = new Merchant("merchant_1", "Acme", "5411", "GB00BANK", true);
        return new Payment(PaymentId.generate(), IdempotencyKey.of("idem-1"), merchant,
                new Money(1000L, Currency.GBP), new PaymentMethod(PaymentMethodType.CARD, "tok_123", "4242"),
                "order description", "order_1", "buyer@example.com", "127.0.0.1");
    }

    @Test
    void newPaymentStartsPending() {
        Payment payment = newPayment();
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PENDING);
    }

    @Test
    void authorizeThenCaptureSucceeds() {
        Payment payment = newPayment();
        payment.authorize(TransactionId.of("txn_1"), RiskScore.of(10));
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.AUTHORIZED);

        payment.capture();
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.CAPTURED);
    }

    @Test
    void cannotCaptureAPendingPayment() {
        Payment payment = newPayment();
        assertThatThrownBy(payment::capture).isInstanceOf(InvalidPaymentStateException.class);
    }

    @Test
    void cannotCaptureATerminalPayment() {
        Payment payment = newPayment();
        payment.authorize(TransactionId.of("txn_1"), RiskScore.of(10));
        payment.cancel();

        assertThatThrownBy(payment::capture).isInstanceOf(InvalidPaymentStateException.class);
    }
}
