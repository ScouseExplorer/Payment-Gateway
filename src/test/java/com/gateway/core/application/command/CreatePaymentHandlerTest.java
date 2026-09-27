package com.gateway.core.application.command;

import com.gateway.core.application.service.MerchantService;
import com.gateway.core.domain.model.Merchant;
import com.gateway.core.domain.service.PaymentProcessor;
import com.gateway.core.infrastructure.database.entity.PaymentEntity;
import com.gateway.core.infrastructure.database.mapper.PaymentMapper;
import com.gateway.core.infrastructure.database.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreatePaymentHandlerTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private MerchantService merchantService;

    private final PaymentProcessor paymentProcessor = new PaymentProcessor();

    private CreatePaymentHandler handler;

    private Merchant merchant;
    private CreatePaymentCommand command;

    @BeforeEach
    void setUp() {
        handler = new CreatePaymentHandler(paymentRepository, merchantService, paymentProcessor);
        merchant = new Merchant("merchant_1", "Acme", "5411", "GB00BANK", true);
        command = new CreatePaymentCommand("idem-1", "merchant_1", 1000L, "GBP", "CARD");
        command.setPaymentMethodToken("tok_123");
        command.setPaymentMethodLast4("4242");
    }

    @Test
    void createsAndAuthorizesNewPayment() {
        when(merchantService.requireActiveMerchant("merchant_1")).thenReturn(merchant);
        when(paymentRepository.findByMerchantIdAndIdempotencyKey("merchant_1", "idem-1"))
                .thenReturn(Optional.empty());

        PaymentCreationResult result = handler.handle(command);

        assertThat(result.isNewlyCreated()).isTrue();
        assertThat(result.getPayment().getStatus().name()).isEqualTo("AUTHORIZED");
        verify(paymentRepository).saveAndFlush(any(PaymentEntity.class));
    }

    @Test
    void replaysExistingPaymentForKnownIdempotencyKey() {
        PaymentEntity existing = PaymentMapper.toEntity(newAuthorizedPayment());
        when(merchantService.requireActiveMerchant("merchant_1")).thenReturn(merchant);
        when(paymentRepository.findByMerchantIdAndIdempotencyKey("merchant_1", "idem-1"))
                .thenReturn(Optional.of(existing));

        PaymentCreationResult result = handler.handle(command);

        assertThat(result.isNewlyCreated()).isFalse();
        verify(paymentRepository, never()).saveAndFlush(any());
    }

    @Test
    void returnsWinnerWhenConcurrentRequestWinsTheUniqueConstraintRace() {
        PaymentEntity winner = PaymentMapper.toEntity(newAuthorizedPayment());
        when(merchantService.requireActiveMerchant("merchant_1")).thenReturn(merchant);
        when(paymentRepository.findByMerchantIdAndIdempotencyKey("merchant_1", "idem-1"))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(winner));
        when(paymentRepository.saveAndFlush(any(PaymentEntity.class)))
                .thenThrow(new DataIntegrityViolationException("duplicate key"));

        PaymentCreationResult result = handler.handle(command);

        assertThat(result.isNewlyCreated()).isFalse();
        assertThat(result.getPayment().getIdempotencyKey().getValue()).isEqualTo("idem-1");
    }

    private com.gateway.core.domain.model.Payment newAuthorizedPayment() {
        com.gateway.core.domain.model.Payment payment = new com.gateway.core.domain.model.Payment(
                com.gateway.core.domain.model.PaymentId.generate(),
                com.gateway.core.domain.model.IdempotencyKey.of("idem-1"),
                merchant,
                new com.gateway.core.domain.model.Money(1000L, com.gateway.core.domain.model.Currency.GBP),
                new com.gateway.core.domain.model.PaymentMethod(
                        com.gateway.core.domain.model.PaymentMethodType.CARD, "tok_123", "4242"),
                null, null, null, null);
        payment.authorize(com.gateway.core.domain.model.TransactionId.of("txn_1"),
                com.gateway.core.domain.model.RiskScore.of(0));
        return payment;
    }
}
