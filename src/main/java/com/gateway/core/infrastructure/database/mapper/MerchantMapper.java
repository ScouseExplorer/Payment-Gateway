package com.gateway.core.infrastructure.database.mapper;

import com.gateway.core.domain.model.Merchant;
import com.gateway.core.infrastructure.database.entity.MerchantEntity;

import java.time.Instant;

public final class MerchantMapper {

    private MerchantMapper() {
    }

    public static MerchantEntity toEntity(Merchant merchant) {
        return new MerchantEntity(merchant.getMerchantId(), merchant.getName(), merchant.getMcc(),
                merchant.getBankAccount(), merchant.isActive(), Instant.now());
    }

    public static Merchant toDomain(MerchantEntity entity) {
        return new Merchant(entity.getMerchantId(), entity.getName(), entity.getMcc(),
                entity.getBankAccount(), entity.isActive());
    }
}
