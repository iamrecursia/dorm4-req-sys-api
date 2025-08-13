-- liquibase formatted sql

-- changeset kozitskiy:001-initial-schema
-- comment: Create initial tables (users and requests) with indexes

CREATE TABLE IF NOT EXISTS users (
    id        SERIAL PRIMARY KEY,
    username  VARCHAR(255) NOT NULL UNIQUE,
    password  VARCHAR(255) NOT NULL,
    user_type VARCHAR(50)  NOT NULL
    );

CREATE TABLE IF NOT EXISTS requests (
    id           SERIAL PRIMARY KEY,
    title        VARCHAR(255) NOT NULL,
    description  TEXT,
    request_type VARCHAR(50)  NOT NULL,
    status       VARCHAR(50)  NOT NULL,
    student_id   BIGINT       NOT NULL,
    worker_id    BIGINT,
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP
    );

-- changeset kozitskiy:001-initial-indexes
-- comment: Create initial indexes for requests table

CREATE INDEX idx_requests_student_id ON requests (student_id);
CREATE INDEX idx_requests_worker_id ON requests (worker_id);
CREATE INDEX idx_requests_status ON requests (status);