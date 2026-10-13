
-- SwizzBank database schema

CREATE TABLE accounts (
    account_number VARCHAR(20) PRIMARY KEY,
    holder_name VARCHAR(100) NOT NULL,
    balance NUMERIC(12, 2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT accounts_balance_nonnegative
        CHECK (balance >= 0),

    CONSTRAINT accounts_holder_name_nonblank
        CHECK (LENGTH(TRIM(holder_name)) > 0)
);

CREATE TABLE transactions (
    transaction_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    account_number VARCHAR(20) NOT NULL,
    type VARCHAR(10) NOT NULL,
    amount NUMERIC(12, 2) NOT NULL,
    timestamp TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    balance_after NUMERIC(12, 2) NOT NULL,

    CONSTRAINT transactions_account_fk
        FOREIGN KEY (account_number)
        REFERENCES accounts(account_number),

    CONSTRAINT transactions_type_valid
        CHECK (type IN ('DEPOSIT', 'WITHDRAWAL')),

    CONSTRAINT transactions_amount_positive
        CHECK (amount > 0),

    CONSTRAINT transactions_balance_nonnegative
        CHECK (balance_after >= 0)
);

CREATE INDEX idx_transactions_account_timestamp
    ON transactions (account_number, timestamp DESC);
