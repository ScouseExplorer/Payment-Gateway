package com.gateway.core.application.service;

import com.gateway.core.domain.model.Merchant;
import com.gateway.core.exception.MerchantInactiveException;
import com.gateway.core.exception.MerchantNotFoundException;
import com.gateway.core.infrastructure.database.entity.MerchantEntity;
import com.gateway.core.infrastructure.database.mapper.MerchantMapper;
import com.gateway.core.infrastructure.database.repository.MerchantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application service for merchant registration and lookup (Stage 1).
 */
@Service
public class MerchantService {

    private final MerchantRepository merchantRepository;

    public MerchantService(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    @Transactional
    public Merchant register(Merchant merchant) {
        merchantRepository.save(MerchantMapper.toEntity(merchant));
        return merchant;
    }

    @Transactional(readOnly = true)
    public Merchant getMerchant(String merchantId) {
        MerchantEntity entity = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new MerchantNotFoundException(merchantId));
        return MerchantMapper.toDomain(entity);
    }

    /**
     * Look up a merchant and ensure it is active before it can be used for payments.
     */
    @Transactional(readOnly = true)
    public Merchant requireActiveMerchant(String merchantId) {
        Merchant merchant = getMerchant(merchantId);
        if (!merchant.isActive()) {
            throw new MerchantInactiveException(merchantId);
        }
        return merchant;
    }
}
