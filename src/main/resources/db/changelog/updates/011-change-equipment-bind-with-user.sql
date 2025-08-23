-- liquibase formatted sql

-- changeset kozitskiy:011-equipment-bind-with-user
-- comment: Обновляем связь оборудования с пользователем

ALTER TABLE equipment ALTER COLUMN room_id DROP NOT NULL;







