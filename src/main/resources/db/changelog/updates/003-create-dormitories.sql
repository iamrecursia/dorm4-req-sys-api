-- liquibase formatted sql

-- changeset kozitskiy:003-create-dormitories
-- comment: Create dormitories table
CREATE TABLE IF NOT EXISTS dormitories (
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    address      VARCHAR(500) NOT NULL,
    floors_count INTEGER      NOT NULL,
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP
    );