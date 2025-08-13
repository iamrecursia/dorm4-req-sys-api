-- liquibase formatted sql

-- changeset kozitskiy:008-add-dormitory-to-users
-- comment: Add dormitory relationship to users table
ALTER TABLE users ADD COLUMN dormitory_id BIGINT;

-- Добавляем внешний ключ
ALTER TABLE users
ADD CONSTRAINT fk_user_dormitory
FOREIGN KEY (dormitory_id) REFERENCES dormitories(id)
ON DELETE SET NULL;