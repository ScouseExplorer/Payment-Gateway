package com.gateway.core.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentStatusTest {

    @Test
    void pendingCanTransitionToAuthorizedOrFailed() {
        assertThat(PaymentStatus.PENDING.canTransitionTo(PaymentStatus.AUTHORIZED)).isTrue();
        assertThat(PaymentStatus.PENDING.canTransitionTo(PaymentStatus.FAILED)).isTrue();
        assertThat(PaymentStatus.PENDING.canTransitionTo(PaymentStatus.CAPTURED)).isFalse();
    }

    @Test
    void authorizedCanTransitionToCapturedCancelledOrFailed() {
        assertThat(PaymentStatus.AUTHORIZED.canTransitionTo(PaymentStatus.CAPTURED)).isTrue();
        assertThat(PaymentStatus.AUTHORIZED.canTransitionTo(PaymentStatus.CANCELLED)).isTrue();
        assertThat(PaymentStatus.AUTHORIZED.canTransitionTo(PaymentStatus.FAILED)).isTrue();
        assertThat(PaymentStatus.AUTHORIZED.canTransitionTo(PaymentStatus.SETTLED)).isFalse();
    }

    @Test
    void terminalStatesRejectAnyTransition() {
        for (PaymentStatus target : PaymentStatus.values()) {
            assertThat(PaymentStatus.CANCELLED.canTransitionTo(target)).isFalse();
            assertThat(PaymentStatus.FAILED.canTransitionTo(target)).isFalse();
            assertThat(PaymentStatus.VOIDED.canTransitionTo(target)).isFalse();
        }
    }
}
