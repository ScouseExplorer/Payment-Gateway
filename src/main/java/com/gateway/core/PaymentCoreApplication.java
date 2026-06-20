package com.gateway.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main entry point for the Payment Core Service application.
 * 
 * This Spring Boot application is responsible for:
 * - Processing payment transactions (authorization, capture, settlement)
 * - Managing payment refunds and reversals
 * - Fraud detection and risk scoring
 * - Merchant account management
 * - Event-driven architecture via Kafka
 * - Multi-channel payment processing (cards, wallets, bank transfers)
 * - Audit logging and compliance
 * - RESTful API and GraphQL endpoints
 * 
 * The application follows:
 * - Domain-Driven Design (DDD) principles
 * - Clean Architecture layers
 * - CQRS pattern for commands and queries
 * - Event sourcing for critical transactions
 * 
 * @author Payment Platform Team
 */
@SpringBootApplication(scanBasePackages = "com.gateway.core")
@EnableJpaRepositories(basePackages = "com.gateway.core.infrastructure.database.repository")
@EnableKafka
@EnableScheduling
public class PaymentCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentCoreApplication.class, args);
    }
}
