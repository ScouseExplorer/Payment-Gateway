package com.gateway.core.infrastructure.database.mapper;

import com.gateway.core.domain.model.IdempotencyKey;
import com.gateway.core.domain.model.Merchant;
import com.gateway.core.domain.model.Money;
import com.gateway.core.domain.model.Payment;
import com.gateway.core.domain.model.PaymentId;
import com.gateway.core.domain.model.PaymentMethod;
import com.gateway.core.domain.model.RiskScore;
import com.gateway.core.domain.model.TransactionId;
import com.gateway.core.infrastructure.database.entity.PaymentEntity;

import java.time.Instant;
import java.time.ZoneOffset;

public final class PaymentMapper {

    private PaymentMapper() {
    }

    public static PaymentEntity toEntity(Payment payment) {
        PaymentMethod method = payment.getPaymentMethod();
        return new PaymentEntity(
                payment.getPaymentId().getValue(),
                payment.getIdempotencyKey().getValue(),
                payment.getMerchant().getMerchantId(),
                payment.getAmount().getAmount(),
                payment.getAmount().getCurrency(),
                method.getType(),
                method.getToken(),
                method.getLast4Digits(),
                payment.getStatus(),
                payment.getTransactionId() != null ? payment.getTransactionId().getValue() : null,
                payment.getRiskScore() != null ? payment.getRiskScore().getScore() : null,
                payment.getDescription(),
                payment.getOrderId(),
                payment.getCustomerEmail(),
                payment.getCustomerIp(),
                toInstant(payment.getCreatedAt()),
                toInstant(payment.getUpdatedAt()));
    }

    public static Payment toDomain(PaymentEntity entity, Merchant merchant) {
        return Payment.reconstruct(
                PaymentId.of(entity.getPaymentId()),
                IdempotencyKey.of(entity.getIdempotencyKey()),
                merchant,
                new Money(entity.getAmount(), entity.getCurrency()),
                new PaymentMethod(entity.getPaymentMethodType(), entity.getPaymentMethodToken(),
                        entity.getPaymentMethodLast4()),
                entity.getStatus(),
                entity.getTransactionId() != null ? TransactionId.of(entity.getTransactionId()) : null,
                entity.getRiskScore() != null ? RiskScore.of(entity.getRiskScore()) : null,
                entity.getDescription(),
                entity.getOrderId(),
                entity.getCustomerEmail(),
                entity.getCustomerIp(),
                toEpochMilli(entity.getCreatedAt()),
                toEpochMilli(entity.getUpdatedAt()));
    }

    private static Instant toInstant(Long epochMilli) {
        return epochMilli == null ? null : Instant.ofEpochMilli(epochMilli).atZone(ZoneOffset.UTC).toInstant();
    }

    private static Long toEpochMilli(Instant instant) {
        return instant == null ? null : instant.toEpochMilli();
    }
}
