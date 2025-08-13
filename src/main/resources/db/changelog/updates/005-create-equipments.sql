-- liquibase formatted sql

-- changeset kozitskiy:005-create-equipment
-- comment: Create equipment table
CREATE TABLE IF NOT EXISTS equipment (
    id             SERIAL PRIMARY KEY,
    name           VARCHAR(255) NOT NULL,
    description    TEXT,
    room_id        BIGINT       NOT NULL,
    status         VARCHAR(50)  NOT NULL,
    last_check_date DATE,
    created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP
    );
