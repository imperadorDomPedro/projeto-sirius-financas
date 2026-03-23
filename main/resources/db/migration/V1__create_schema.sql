-- V1__create_schema.sql

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- USERS
CREATE TABLE users (
    id            UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name          VARCHAR(100) NOT NULL,
    email         VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    currency      VARCHAR(3) NOT NULL DEFAULT 'BRL',
    active        BOOLEAN NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP NOT NULL DEFAULT NOW()
);

-- ACCOUNTS
CREATE TABLE accounts (
    id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id    UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name       VARCHAR(100) NOT NULL,
    type       VARCHAR(30) NOT NULL CHECK (type IN ('CHECKING','SAVINGS','CASH','INVESTMENT','CREDIT_CARD')),
    balance    NUMERIC(15,2) NOT NULL DEFAULT 0.00,
    color      VARCHAR(7),
    active     BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- CATEGORIES
CREATE TABLE categories (
    id        UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id   UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name      VARCHAR(80) NOT NULL,
    type      VARCHAR(10) NOT NULL CHECK (type IN ('INCOME','EXPENSE')),
    color     VARCHAR(7),
    icon      VARCHAR(50),
    parent_id UUID REFERENCES categories(id)
);

-- RECURRENCES
CREATE TABLE recurrences (
    id            UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id       UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    frequency     VARCHAR(20) NOT NULL CHECK (frequency IN ('DAILY','WEEKLY','MONTHLY','YEARLY')),
    start_date    DATE NOT NULL,
    end_date      DATE,
    day_of_month  INTEGER CHECK (day_of_month BETWEEN 1 AND 31),
    active        BOOLEAN NOT NULL DEFAULT TRUE
);

-- TRANSACTIONS
CREATE TABLE transactions (
    id               UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    account_id       UUID NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
    category_id      UUID REFERENCES categories(id),
    recurrence_id    UUID REFERENCES recurrences(id),
    amount           NUMERIC(15,2) NOT NULL CHECK (amount > 0),
    type             VARCHAR(10) NOT NULL CHECK (type IN ('INCOME','EXPENSE')),
    description      VARCHAR(255),
    transaction_date DATE NOT NULL,
    status           VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED' CHECK (status IN ('CONFIRMED','PENDING','CANCELLED')),
    created_at       TIMESTAMP NOT NULL DEFAULT NOW()
);

-- BUDGETS
CREATE TABLE budgets (
    id            UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id       UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    category_id   UUID NOT NULL REFERENCES categories(id),
    limit_amount  NUMERIC(15,2) NOT NULL CHECK (limit_amount > 0),
    spent_amount  NUMERIC(15,2) NOT NULL DEFAULT 0.00,
    month         INTEGER NOT NULL CHECK (month BETWEEN 1 AND 12),
    year          INTEGER NOT NULL,
    UNIQUE (user_id, category_id, month, year)
);

-- GOALS
CREATE TABLE goals (
    id             UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id        UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name           VARCHAR(100) NOT NULL,
    target_amount  NUMERIC(15,2) NOT NULL CHECK (target_amount > 0),
    current_amount NUMERIC(15,2) NOT NULL DEFAULT 0.00,
    deadline       DATE,
    status         VARCHAR(20) NOT NULL DEFAULT 'IN_PROGRESS' CHECK (status IN ('IN_PROGRESS','COMPLETED','CANCELLED'))
);

-- TRANSFERS
CREATE TABLE transfers (
    id                UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    origin_account_id UUID NOT NULL REFERENCES accounts(id),
    dest_account_id   UUID NOT NULL REFERENCES accounts(id),
    amount            NUMERIC(15,2) NOT NULL CHECK (amount > 0),
    transfer_date     DATE NOT NULL,
    description       VARCHAR(255),
    created_at        TIMESTAMP NOT NULL DEFAULT NOW(),
    CHECK (origin_account_id <> dest_account_id)
);

-- INDEXES
CREATE INDEX idx_transactions_account   ON transactions(account_id);
CREATE INDEX idx_transactions_category  ON transactions(category_id);
CREATE INDEX idx_transactions_date      ON transactions(transaction_date);
CREATE INDEX idx_accounts_user          ON accounts(user_id);
CREATE INDEX idx_budgets_user_month     ON budgets(user_id, month, year);
