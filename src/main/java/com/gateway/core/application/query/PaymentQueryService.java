package com.gateway.core.application.query;

import com.gateway.core.domain.model.Merchant;
import com.gateway.core.domain.model.Payment;
import com.gateway.core.exception.PaymentNotFoundException;
import com.gateway.core.infrastructure.database.entity.PaymentEntity;
import com.gateway.core.infrastructure.database.mapper.MerchantMapper;
import com.gateway.core.infrastructure.database.mapper.PaymentMapper;
import com.gateway.core.infrastructure.database.repository.MerchantRepository;
import com.gateway.core.infrastructure.database.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Read-only lookups for payments (the "Q" side of CQRS for Stage 1).
 */
@Service
public class PaymentQueryService {

    private final PaymentRepository paymentRepository;
    private final MerchantRepository merchantRepository;

    public PaymentQueryService(PaymentRepository paymentRepository, MerchantRepository merchantRepository) {
        this.paymentRepository = paymentRepository;
        this.merchantRepository = merchantRepository;
    }

    @Transactional(readOnly = true)
    public Payment getPayment(String paymentId) {
        PaymentEntity entity = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
        Merchant merchant = MerchantMapper.toDomain(merchantRepository.getReferenceById(entity.getMerchantId()));
        return PaymentMapper.toDomain(entity, merchant);
    }
}
