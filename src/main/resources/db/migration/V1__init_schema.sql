-- Stage 1: Payment Core schema (merchants + payments)

CREATE TABLE merchants (
    merchant_id  VARCHAR(64) PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    mcc          VARCHAR(10),
    bank_account VARCHAR(255),
    active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at   TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE payments (
    payment_id           VARCHAR(64) PRIMARY KEY,
    idempotency_key      VARCHAR(255) NOT NULL,
    merchant_id          VARCHAR(64) NOT NULL REFERENCES merchants (merchant_id),
    amount               BIGINT NOT NULL CHECK (amount >= 0),
    currency             VARCHAR(3) NOT NULL,
    payment_method_type  VARCHAR(30) NOT NULL,
    payment_method_token VARCHAR(255),
    payment_method_last4 VARCHAR(4),
    status               VARCHAR(20) NOT NULL,
    transaction_id       VARCHAR(255),
    risk_score           INTEGER CHECK (risk_score BETWEEN 0 AND 100),
    description          TEXT,
    order_id             VARCHAR(255),
    customer_email       VARCHAR(255),
    customer_ip          VARCHAR(64),
    created_at           TIMESTAMPTZ NOT NULL,
    updated_at           TIMESTAMPTZ,
    CONSTRAINT uq_payments_merchant_idempotency UNIQUE (merchant_id, idempotency_key)
);

CREATE INDEX idx_payments_merchant_id ON payments (merchant_id);
CREATE INDEX idx_payments_status ON payments (status);
