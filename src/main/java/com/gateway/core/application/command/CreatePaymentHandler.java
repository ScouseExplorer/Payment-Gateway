package com.gateway.core.application.command;

import com.gateway.core.application.service.MerchantService;
import com.gateway.core.domain.model.Currency;
import com.gateway.core.domain.model.IdempotencyKey;
import com.gateway.core.domain.model.Merchant;
import com.gateway.core.domain.model.Money;
import com.gateway.core.domain.model.Payment;
import com.gateway.core.domain.model.PaymentId;
import com.gateway.core.domain.model.PaymentMethod;
import com.gateway.core.domain.model.PaymentMethodType;
import com.gateway.core.domain.service.PaymentProcessor;
import com.gateway.core.infrastructure.database.entity.PaymentEntity;
import com.gateway.core.infrastructure.database.mapper.PaymentMapper;
import com.gateway.core.infrastructure.database.repository.PaymentRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handles {@link CreatePaymentCommand}.
 * 
 * Idempotency (Section 6) is enforced in two layers:
 * 1. An upfront lookup by (merchantId, idempotencyKey) short-circuits repeats.
 * 2. A DB unique constraint catches the race where two concurrent requests
 *    with the same key both miss the lookup; the loser re-reads and returns
 *    the winner's result instead of erroring.
 */
@Service
public class CreatePaymentHandler {

    private final PaymentRepository paymentRepository;
    private final MerchantService merchantService;
    private final PaymentProcessor paymentProcessor;

    public CreatePaymentHandler(PaymentRepository paymentRepository, MerchantService merchantService,
                                 PaymentProcessor paymentProcessor) {
        this.paymentRepository = paymentRepository;
        this.merchantService = merchantService;
        this.paymentProcessor = paymentProcessor;
    }

    @Transactional
    public PaymentCreationResult handle(CreatePaymentCommand command) {
        Merchant merchant = merchantService.requireActiveMerchant(command.getMerchantId());

        PaymentEntity existing = paymentRepository
                .findByMerchantIdAndIdempotencyKey(merchant.getMerchantId(), command.getIdempotencyKey())
                .orElse(null);
        if (existing != null) {
            return new PaymentCreationResult(PaymentMapper.toDomain(existing, merchant), false);
        }

        Payment payment = new Payment(
                PaymentId.generate(),
                IdempotencyKey.of(command.getIdempotencyKey()),
                merchant,
                new Money(command.getAmount(), Currency.valueOf(command.getCurrency())),
                new PaymentMethod(PaymentMethodType.valueOf(command.getPaymentMethodType()),
                        command.getPaymentMethodToken(), command.getPaymentMethodLast4()),
                command.getDescription(),
                command.getOrderId(),
                command.getCustomerEmail(),
                command.getCustomerIp());

        paymentProcessor.authorize(payment);

        try {
            paymentRepository.saveAndFlush(PaymentMapper.toEntity(payment));
        } catch (DataIntegrityViolationException raceLost) {
            PaymentEntity winner = paymentRepository
                    .findByMerchantIdAndIdempotencyKey(merchant.getMerchantId(), command.getIdempotencyKey())
                    .orElseThrow(() -> raceLost);
            return new PaymentCreationResult(PaymentMapper.toDomain(winner, merchant), false);
        }

        return new PaymentCreationResult(payment, true);
    }
}
