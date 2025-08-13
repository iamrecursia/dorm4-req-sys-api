-- liquibase formatted sql

-- changeset kozitskiy:004-create-rooms
-- comment: Create rooms table with foreign key to dormitories
CREATE TABLE IF NOT EXISTS rooms(
    id SERIAL PRIMARY KEY,
    number VARCHAR(20) NOT NULL,
    dormitory_id BIGINT NOT NULL,
    capacity INTEGER,
    current_occupancy INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
    );
