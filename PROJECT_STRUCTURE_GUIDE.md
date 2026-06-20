# Payment Core Service - Project Structure Guide

## Overview

This document provides a comprehensive briefing of each major component and file in the Payment Core Service project.

---

## 📁 Root-Level Configuration Files

### `pom.xml`
**Purpose**: Maven Project Object Model  
**Contains**: Project dependencies, build plugins, version management, properties  
**Key Dependencies**: Spring Boot, Spring Data JPA, Kafka, PostgreSQL driver, JWT libraries, testing frameworks

### `README.md`
**Purpose**: Project documentation and quick start guide  
**Contains**: Architecture overview, project structure explanation, quick start instructions, CI/CD pipeline info

### `Dockerfile`
**Purpose**: Container image definition for deployment  
**Contains**: Multi-stage build (compilation + runtime), base image, health checks, JVM configuration  
**Key Features**: Non-root user execution, minimal Alpine image for runtime, health check endpoints

### `docker-compose.yml`
**Purpose**: Local development environment orchestration  
**Contains**: PostgreSQL, Kafka, Zookeeper, Redis, application service definitions  
**Usage**: `docker-compose up` for full local stack

### `.gitignore`
**Purpose**: Specifies files/directories to exclude from version control  
**Contains**: Maven target/, IDE configs, OS files, environment files, logs

### `.editorconfig`
**Purpose**: Maintains consistent coding styles across editors  
**Contains**: Indentation rules (2 spaces for XML/YAML, 4 for Java), line endings, character encoding

### `.env.example`
**Purpose**: Template for environment variables  
**Contains**: Database config, API keys, service endpoints, feature flags  
**Usage**: Copy to `.env` and fill with actual values

### `sonar-project.properties`
**Purpose**: SonarQube code quality analysis configuration  
**Contains**: Project key, source paths, coverage report paths, exclusions  
**Usage**: Integrated into CI/CD pipeline for code quality gates

---

## 🔄 CI/CD Workflows (`.github/workflows/`)

### `build.yml` - Build and Test Pipeline
**Triggers**: Push to main/develop, pull requests  
**Steps**:
1. Checkout code with full history
2. Setup Java 17
3. Build with Maven (skipTests)
4. Run all tests
5. Upload test results artifacts
6. Publish test summary

**Output**: Compiled JAR, test reports

### `test.yml` - Comprehensive Test Suites
**Triggers**: Push to main/develop, pull requests  
**Test Categories**:
- **Unit Tests**: Isolated component tests
- **Integration Tests**: API & database tests with PostgreSQL/Kafka services
- **Architecture Tests**: Verify clean architecture layer separation
- **Performance Tests**: Load testing (runs only on main branch)

**Services**: PostgreSQL, Kafka, Zookeeper for integration tests  
**Artifacts**: Coverage reports, performance results

### `security-scan.yml` - Security Analysis Pipeline
**Triggers**: Push/PR, scheduled weekly  
**Scans**:
- **SonarQube**: Code quality, security hotspots, code smells
- **OWASP Dependency-Check**: Vulnerable dependency detection
- **Trivy**: Container image vulnerability scanning

**Output**: SARIF reports uploaded to GitHub Security tab

### `release.yml` - Release and Deployment Pipeline
**Triggers**: Git tag push (v*.*.*)  
**Steps**:
1. Build release JAR
2. Create GitHub Release
3. Upload JAR artifact
4. Build and push Docker image
5. Deploy to Kubernetes via Helm
6. Update image tag in Helm values

**Requires**: Docker registry credentials, Kubernetes credentials

---

## 🎯 Helm Deployment (`.helm/`)

### `Chart.yaml`
**Purpose**: Helm chart metadata  
**Contains**: Chart name, version, appVersion, maintainers, keywords

### `values.yaml`
**Purpose**: Default Helm configuration and customization  
**Configuration Areas**:
- Replica count (3 for HA)
- Image repository and pull policy
- Resource limits/requests
- Autoscaling rules (min 3, max 10 replicas)
- Health check probes (liveness/readiness)
- Ingress configuration
- Database connection strings
- Kafka and Redis settings
- Security context (non-root user)
- Node affinity and pod anti-affinity rules

**Features**: PVC setup, metrics scraping labels, cost optimization

---

## ☸️ Kubernetes Manifests (`./k8s/`)

### `deployment.yaml`
**Purpose**: Kubernetes deployment configuration  
**Key Specs**:
- 3 replicas for high availability
- Rolling update strategy
- Security context (non-root, read-only filesystem)
- Liveness probe: `/actuator/health/liveness` (30s initial delay)
- Readiness probe: `/actuator/health/readiness` (10s initial delay)
- Resource limits: 1000m CPU, 512Mi memory
- Pod anti-affinity to spread across nodes
- Environment variables from ConfigMap and Secrets

### `service.yaml`
**Purpose**: Kubernetes service for internal communication  
**Type**: ClusterIP (internal-only)  
**Ports**: 8080 for HTTP traffic  
**Selector**: Routes traffic to payment-core-service pods

### `ingress.yaml`
**Purpose**: External HTTP/HTTPS access configuration  
**Features**:
- HTTPS with Let's Encrypt certificates
- Rate limiting (100 requests)
- SSL redirect enabled
- Host-based routing

### `configmap.yaml`
**Purpose**: Non-sensitive configuration data  
**Contains**:
- Database URL and Kafka bootstrap servers
- Topic names
- Redis connection details
- Logging levels
- Actuator endpoint exposure

### `secrets.yaml`
**Purpose**: Sensitive credentials storage  
**Contains**:
- Database username/password
- JWT secret key
- API keys
- Encryption key

---

## 📦 Java Application Structure

### Application Entry Point

#### `PaymentCoreApplication.java`
**Purpose**: Spring Boot application bootstrap class  
**Annotations**: @SpringBootApplication, @EnableJpaRepositories, @EnableKafka, @EnableScheduling  
**Responsibilities**:
- Application startup
- Component scanning
- JPA repository initialization
- Kafka listener registration
- Scheduled task enablement

---

## 🌐 API Layer (`src/main/java/com/gateway/core/api/rest/`)

### Controllers

#### `PaymentController.java`
**REST Endpoints**:
- `POST /api/payments` - Create payment
- `GET /api/payments/{id}` - Get payment details
- `POST /api/payments/{id}/capture` - Capture authorized payment
- `POST /api/payments/{id}/cancel` - Cancel pending payment
- `GET /api/payments` - Search payments

**Responsibilities**: HTTP request handling, input validation, response formatting

#### `RefundController.java`
**REST Endpoints**:
- `POST /api/refunds` - Create refund
- `GET /api/refunds/{id}` - Get refund status
- `GET /api/payments/{id}/refunds` - List refunds for payment

**Responsibilities**: Refund request handling, refund tracking

#### `SettlementController.java`
**REST Endpoints**:
- `POST /api/settlements` - Process settlement batch
- `GET /api/settlements/{id}` - Get settlement status
- `GET /api/settlements/{id}/reconciliation` - Get reconciliation report

**Responsibilities**: Settlement processing, batch reconciliation

#### `MerchantController.java`
**REST Endpoints**:
- `POST /api/merchants` - Register merchant
- `GET /api/merchants/{id}` - Get merchant info
- `PUT /api/merchants/{id}` - Update merchant
- `GET /api/merchants/{id}/settings` - Get merchant settings

**Responsibilities**: Merchant account management

#### `HealthController.java`
**REST Endpoints**:
- `GET /actuator/health` - Overall health
- `GET /actuator/health/liveness` - Kubernetes liveness probe
- `GET /actuator/health/readiness` - Kubernetes readiness probe

**Responsibilities**: Application health reporting for monitoring

### Request/Response Objects
- `CreatePaymentRequest.java`: Payment creation DTO
- `RefundRequest.java`: Refund request DTO
- `CaptureRequest.java`: Capture request DTO
- `PaymentResponse.java`: Payment API response
- `RefundResponse.java`: Refund API response
- `ErrorResponse.java`: Standardized error response

### Exception Handling
- `RestExceptionHandler.java`: Converts exceptions to HTTP responses

---

## ⚙️ Application Service Layer (`src/main/java/com/gateway/core/application/`)

### CQRS Pattern Implementation

#### Commands (Command Query Responsibility Segregation)
**Purpose**: Encapsulate state-changing operations

- `CreatePaymentCommand.java`: Initiate payment (data: merchant ID, amount, currency, payment method)
- `CapturePaymentCommand.java`: Capture authorized funds
- `RefundPaymentCommand.java`: Refund captured payment
- `CancelPaymentCommand.java`: Cancel pending payment
- `VoidPaymentCommand.java`: Reverse authorized payment

#### Query Objects
- `GetPaymentQuery.java`: Retrieve single payment
- `SearchPaymentsQuery.java`: Search with filters (date, merchant, status)
- `MerchantPaymentsQuery.java`: Get payments for merchant

#### Command Handlers
- `CreatePaymentHandler.java`: Execute CreatePaymentCommand
- `CapturePaymentHandler.java`: Execute CapturePaymentCommand
- `RefundPaymentHandler.java`: Execute RefundPaymentCommand
- `SearchPaymentsHandler.java`: Execute SearchPaymentsQuery

#### Application Services
- `PaymentApplicationService.java`: High-level payment operations
- `RefundApplicationService.java`: Refund orchestration
- `SettlementApplicationService.java`: Settlement batch processing

#### Mappers
- `PaymentMapper.java`: Convert between domain and API DTOs

---

## 🎯 Domain Layer (`src/main/java/com/gateway/core/domain/`)

### Domain Models

#### `Payment.java` - Aggregate Root
**Responsibilities**:
- Represents complete payment lifecycle
- Enforces business rules
- Manages state transitions
- Publishes domain events
- Core fields: paymentId, amount, currency, status, riskScore, merchant

**Key Methods**:
- `authorize()`: Authorize with payment provider
- `capture()`: Capture authorized funds
- `cancel()`: Cancel pending payment
- `void()`: Reverse authorized payment

#### `Money.java` - Value Object
**Responsibilities**:
- Type-safe monetary amount representation
- Prevents currency mixing
- Arithmetic operations (add, subtract)
- Fields: amount (in cents), currency

#### `Currency.java` - Enum
**Supported Currencies**: USD, EUR, GBP, JPY, CNY, INR, AUD, CAD, SGD, HKD  
**Per Currency**: Display name, decimal places for rounding

#### `PaymentStatus.java` - State Enum
**States**:
- PENDING → AUTHORIZED → CAPTURED → SETTLED
- Alternative: FAILED, CANCELLED, REFUNDED, VOIDED
- `canTransitionTo()`: Validates legal state transitions

#### Other Value Objects
- `PaymentId`: Unique payment identifier
- `IdempotencyKey`: Prevents duplicate payments
- `TransactionId`: External provider transaction ID
- `RiskScore`: Fraud risk scoring (0-100)
- `Merchant`: Merchant information aggregate
- `CardToken`: Tokenized card representation
- `PaymentMethod`: Card, wallet, bank transfer type

### Domain Events

Events published for async processing:

- `PaymentCreatedEvent.java`: Triggers fraud analysis
- `PaymentAuthorisedEvent.java`: Indicates authorization success
- `PaymentCapturedEvent.java`: Triggers settlement
- `PaymentRefundedEvent.java`: Triggers accounting updates
- `PaymentCancelledEvent.java`: Cancellation notification
- `PaymentFailedEvent.java`: Failure handling

### Domain Services

#### `PaymentProcessor.java`
**Responsibilities**:
- Orchestrate payment authorization workflow
- Manage payment state transitions
- Coordinate with external gateways
- Invoke fraud detection
- Handle retries and failures

**Methods**:
- `processPayment()`: Main payment authorization
- `capturePayment()`: Capture flow
- `cancelPayment()`: Cancellation logic

#### `FraudEngine.java`
**Responsibilities**:
- Real-time fraud risk analysis
- Machine learning model integration
- Decline decisions

**Risk Scoring Algorithm**:
- Velocity checks (transactions per time window)
- Geographic anomalies (impossible travel)
- Amount outliers
- Merchant category mismatches
- Card not present factors

**Methods**:
- `calculateRiskScore()`: Returns 0-100 score
- `requires3dSecure()`: 3D Secure requirement
- `shouldDecline()`: Automatic decline decision

#### `SettlementEngine.java`
**Responsibilities**:
- Batch settlement processing
- Fund transfer orchestration
- Merchant reconciliation

**Methods**:
- `processBatchSettlement()`: Daily batch processing
- `calculateSettlementAmount()`: Fee calculations
- `reconcile()`: Provider data verification

#### `RefundProcessor.java`
**Responsibilities**:
- Refund transaction processing
- Ledger reversals
- Settlement adjustments

### Repository Interfaces (Hexagonal Port Pattern)
- `PaymentRepository.java`: Payment persistence
- `MerchantRepository.java`: Merchant data access
- `AuditRepository.java`: Audit trail storage

### External Integration Ports (Interfaces)
- `PaymentGatewayPort.java`: External payment processor abstraction
- `LedgerPort.java`: General ledger integration
- `EventPublisher.java`: Event publishing abstraction
- `FraudPort.java`: Fraud service integration
- `NotificationPort.java`: Email/SMS notifications
- `ExchangeRatePort.java`: Currency conversion

### Validation
- `PaymentValidator.java`: Payment business rule validation
- `CardValidator.java`: Card number and expiry validation
- `MerchantValidator.java`: Merchant eligibility checks

### Exception Classes
- Domain-specific exceptions (custom exceptions)
- Validation exceptions
- Business rule violation exceptions

---

## 🔌 Infrastructure Layer (`src/main/java/com/gateway/core/infrastructure/`)

### Database Adapter (`database/`)

- **entity/**: JPA entity classes (mapped to database tables)
- **repository/**: Spring Data JPA repository implementations
- **adapter/**: Domain-to-entity mapping adapters
- **mapper/**: JPA entity mappers

### Kafka Integration (`kafka/`)

#### Producer (`PaymentEventsProducer.java`)
**Responsibilities**: Publish domain events to Kafka
**Topics**: payment.events, settlement.events
**Guarantees**: At-least-once delivery, idempotency with idempotency keys

#### Consumer (`PaymentEventsConsumer.java`)
**Responsibilities**: Listen to events and trigger handlers
**Features**: 
- Error handling with dead letter queues
- Consumer group management
- Exactly-once processing semantics

#### Configuration (`config/`)
- Kafka producer configuration
- Kafka consumer configuration
- Serialization settings

### External Payment Gateway Clients (`client/`)

#### `visa/VisaApiClient.java`
- Authorize payments
- Capture funds
- Process refunds
- API: mutual TLS, request/response encryption

#### `mastercard/MastercardApiClient.java`
- Similar API to Visa
- Mastercard-specific error codes

#### `paypal/PayPalClient.java`
- PayPal integration for digital wallet payments

#### `bank/BankApiClient.java`
- Bank transfer and ACH processing

#### `ledger/LedgerClient.java`
- Integration with general ledger system
- Financial record posting

### Redis Caching (`redis/`)
- Payment lookup cache
- Session storage
- Token blacklist (revoked tokens)
- Rate limiting counters

### Security (`security/`)
- Encryption/decryption utilities
- Key management
- PCI compliance measures

### Scheduler (`scheduler/`)
- Settlement batch jobs
- Reconciliation processes
- Cleanup tasks
- Webhook retry scheduling

### Notification Service (`notification/`)
- Email notifications (payment confirmed, refund issued)
- SMS notifications
- Webhook callbacks to merchants

### Audit Logging (`audit/`)
- Transaction audit trails
- User action logging
- Compliance reporting

### Configuration (`config/`)
- Spring Bean configurations
- Third-party library setups
- Feature flag definitions

---

## 🔐 Security Layer (`src/main/java/com/gateway/core/security/`)

### `JwtAuthenticationFilter.java`
**Responsibilities**:
- Extract JWT from Authorization header
- Validate token signature and expiration
- Set authenticated principal
- Methods**: validateToken(), extractUserId()

### `ApiKeyFilter.java`
**Responsibilities**:
- Alternative authentication via API key
- Machine-to-machine integration
- Permission validation

### `SignatureVerifier.java`
**Responsibilities**:
- Verify webhook signatures from payment providers
- HMAC-SHA256, RSA, ECDSA algorithm support
- Methods**: verifyHmacSignature(), verifyRsaSignature(), verifyWebhookSignature()

### `EncryptionService.java`
**Responsibilities**:
- AES-256 encryption at rest
- Card number masking
- Password hashing (bcrypt)
- Methods**: encrypt(), decrypt(), maskCardNumber(), hashPassword()

### `SecurityConfig.java`
- OAuth2 resource server configuration
- CORS settings
- Security headers

---

## 📊 Monitoring & Observability (`src/main/java/com/gateway/core/monitoring/`)

### `MetricsConfiguration.java`
**Purpose**: Micrometer metrics for Prometheus  
**Tracked Metrics**:
- Payment processing duration (histogram)
- Payment success/failure counts (counter)
- API endpoint latencies
- Database pool metrics
- JVM metrics (memory, GC)

### `TracingConfiguration.java`
**Purpose**: Distributed tracing with Spring Cloud Sleuth + Jaeger/Zipkin  
**Features**:
- Trace ID propagation
- Service dependency mapping
- Cross-service request tracking

### `CorrelationIdFilter.java`
**Purpose**: Request correlation for end-to-end tracing  
**Features**:
- Generate UUID if missing
- Add X-Correlation-Id to responses
- Propagate to downstream calls

### `AuditLogger.java`
**Purpose**: Compliance audit trail  
**Logged Events**:
- Payment operations
- User authentication
- Configuration changes
- Data access
- Suspicious activities

---

## ⚙️ Common Utilities (`src/main/java/com/gateway/core/common/`)

### Constants
- Payment status constants
- Error code constants
- API endpoint paths
- Kafka topic names

### Utilities
- Date/time helpers
- String formatting
- Number formatting
- Serialization helpers

### Clock
- Injectable clock for testing
- Time-based operations

### ID Generators
- UUID generation
- Sequence-based IDs
- Idempotency key generation

---

## ⚠️ Exception Handling (`src/main/java/com/gateway/core/exception/`)

### `GlobalExceptionHandler.java`
**Purpose**: Centralized exception-to-HTTP-response conversion  
**Handles**:
- Validation errors → 400 Bad Request
- Not found errors → 404 Not Found
- Unauthorized → 401 Unauthorized
- Payment errors → 402/422 Payment Required/Unprocessable Entity
- Server errors → 500 Internal Server Error

### `ErrorCodes.java`
**Purpose**: Standardized error code constants  
**Categories**:
- Payment Errors (4000-4999): PAYMENT_NOT_FOUND, PAYMENT_INVALID_STATE
- Validation (4100-4199): INVALID_AMOUNT, INVALID_CARD_NUMBER
- Fraud (4200-4299): FRAUD_DETECTED, VELOCITY_EXCEEDED
- Authorization (4300-4399): UNAUTHORIZED, INVALID_API_KEY
- Merchant (4400-4499): MERCHANT_NOT_FOUND, MERCHANT_INACTIVE
- External Service (5000-5999): GATEWAY_UNAVAILABLE, DATABASE_ERROR

---

## 📋 Configuration Files (`src/main/resources/`)

### `application.yml`
**Master configuration** with environment variable placeholders:
- Database connection (PostgreSQL)
- Kafka configuration
- Redis settings
- JWT settings
- CORS configuration
- Logging levels
- Payment processing timeouts
- Rate limiting
- Feature flags

### `application-dev.yml`
**Development profile**: 
- Local database
- Debug logging
- All actuator endpoints exposed
- Reduced security for testing

### `application-test.yml`
**Test profile**:
- Test database
- Reduced logging
- Limited actuator endpoints

### `application-prod.yml`
**Production profile**:
- Connection pooling optimization
- Kafka durability settings
- Limited logging
- Prometheus metrics export
- File-based logging

---

## 🧪 Test Structure (`src/test/java/com/gateway/core/`)

### Test Categories

- **unit/**: Isolated unit tests for services and validators
- **integration/**: API & database tests with real PostgreSQL/Kafka
- **architecture/**: ArchUnit tests for layer separation
- **contract/**: Consumer-driven contract tests for external APIs
- **performance/**: JMH benchmarks and load testing
- **security/**: Security-focused tests (auth, encryption)
- **e2e/**: Full end-to-end workflow tests
- **fixtures/**: Reusable test data factories
- **builders/**: Builder pattern for complex test objects
- **testcontainers/**: Docker container management for tests

---

## 🚀 Workflow Summary

### Local Development
```bash
docker-compose up        # Start PostgreSQL, Kafka, Redis
mvn spring-boot:run      # Start application
```

### Build & Test
```bash
mvn clean package        # Compile, run tests, create JAR
mvn test                 # Run unit tests only
mvn verify               # Full build with all tests
```

### Deploy to Kubernetes
```bash
docker build -t payment-core-service:latest .
docker push <registry>/payment-core-service:latest
helm upgrade --install payment-core-service ./helm
```

### Payment Processing Flow
1. Client → POST /api/payments (CreatePaymentRequest)
2. PaymentController → CreatePaymentCommand
3. CreatePaymentHandler → Domain validation
4. FraudEngine → Risk score calculation
5. PaymentProcessor → External gateway authorization
6. PaymentCreatedEvent → Kafka topic
7. SettlementEngine → Capture and settlement
8. PaymentCapturedEvent → Async ledger update
9. Response → Client with payment details

---

## 📊 Architecture Layers

```
┌─────────────────────────────────────┐
│  REST API / GraphQL Controllers     │  ← HTTP entry points
├─────────────────────────────────────┤
│  Application Services               │  ← CQRS commands/queries
├─────────────────────────────────────┤
│  Domain Layer (Models/Services)     │  ← Business logic
├─────────────────────────────────────┤
│  Infrastructure Adapters            │  ← Database, Kafka, clients
├─────────────────────────────────────┤
│  Security & Monitoring              │  ← Cross-cutting concerns
└─────────────────────────────────────┘
```

---

## 🔑 Key Design Patterns

1. **Domain-Driven Design**: Bounded contexts, ubiquitous language
2. **Clean Architecture**: Dependency inversion, independence from frameworks
3. **CQRS**: Separation of reads (queries) and writes (commands)
4. **Event Sourcing**: Domain events for state changes
5. **Repository Pattern**: Data abstraction
6. **Value Objects**: Type safety for amounts, statuses
7. **Builder Pattern**: Complex object construction
8. **Hexagonal/Ports-Adapters**: External service abstraction

---

## 📈 Monitoring & Alerting

- **Metrics**: Prometheus endpoint at `/actuator/prometheus`
- **Health**: Liveness/readiness at `/actuator/health/*`
- **Tracing**: Jaeger/Zipkin for distributed requests
- **Audit**: Comprehensive audit logs for compliance
- **Dashboards**: Grafana for visualization

---

## 🔒 Security Features

- JWT-based authentication
- API key validation
- Request signature verification
- Data encryption (AES-256 at rest)
- Card number masking and PCI compliance
- Rate limiting
- CORS configuration
- Non-root container execution
- Read-only root filesystem in Kubernetes

---

## 📞 Support & Maintenance

- CI/CD pipelines run on every commit
- Code quality gates via SonarQube
- Security scanning (OWASP, Trivy)
- Automated deployment to Kubernetes
- Comprehensive logging and monitoring
- Contract tests ensure integration stability

