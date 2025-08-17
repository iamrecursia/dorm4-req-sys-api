-- liquibase formatted sql

-- changeset kozitskiy:010-add-token-field-in-user
-- comment: Добавляем token поле в user

ALTER TABLE users ADD COLUMN token VARCHAR(512)