-- liquibase formatted sql

-- changeset kozitskiy:002-add-check-constraints
-- comment: Добавляем CHECK-ограничения для таблиц

ALTER TABLE requests
    ADD CONSTRAINT chk_worker_id
        CHECK (worker_id > 0 OR worker_id IS NULL);

ALTER TABLE requests
    ADD CONSTRAINT chk_timestamps
        CHECK (created_at <= COALESCE(updated_at, CURRENT_TIMESTAMP));