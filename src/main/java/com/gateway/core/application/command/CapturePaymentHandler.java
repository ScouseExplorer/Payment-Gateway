package com.gateway.core.application.command;

import com.gateway.core.domain.model.Merchant;
import com.gateway.core.domain.model.Payment;
import com.gateway.core.domain.service.PaymentProcessor;
import com.gateway.core.exception.PaymentNotFoundException;
import com.gateway.core.infrastructure.database.entity.PaymentEntity;
import com.gateway.core.infrastructure.database.mapper.MerchantMapper;
import com.gateway.core.infrastructure.database.mapper.PaymentMapper;
import com.gateway.core.infrastructure.database.repository.MerchantRepository;
import com.gateway.core.infrastructure.database.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handles {@link CapturePaymentCommand}: captures a previously authorized payment.
 */
@Service
public class CapturePaymentHandler {

    private final PaymentRepository paymentRepository;
    private final MerchantRepository merchantRepository;
    private final PaymentProcessor paymentProcessor;

    public CapturePaymentHandler(PaymentRepository paymentRepository, MerchantRepository merchantRepository,
                                  PaymentProcessor paymentProcessor) {
        this.paymentRepository = paymentRepository;
        this.merchantRepository = merchantRepository;
        this.paymentProcessor = paymentProcessor;
    }

    @Transactional
    public Payment handle(CapturePaymentCommand command) {
        PaymentEntity entity = paymentRepository.findById(command.getPaymentId())
                .orElseThrow(() -> new PaymentNotFoundException(command.getPaymentId()));
        Merchant merchant = MerchantMapper.toDomain(merchantRepository.getReferenceById(entity.getMerchantId()));

        Payment payment = PaymentMapper.toDomain(entity, merchant);
        paymentProcessor.capturePayment(payment);
        paymentRepository.save(PaymentMapper.toEntity(payment));
        return payment;
    }
}
