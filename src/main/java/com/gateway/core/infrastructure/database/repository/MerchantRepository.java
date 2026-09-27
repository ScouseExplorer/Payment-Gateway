package com.gateway.core.infrastructure.database.repository;

import com.gateway.core.infrastructure.database.entity.MerchantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantRepository extends JpaRepository<MerchantEntity, String> {
}
