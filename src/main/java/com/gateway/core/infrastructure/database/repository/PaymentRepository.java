package com.gateway.core.infrastructure.database.repository;

import com.gateway.core.infrastructure.database.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {

    Optional<PaymentEntity> findByMerchantIdAndIdempotencyKey(String merchantId, String idempotencyKey);
}
