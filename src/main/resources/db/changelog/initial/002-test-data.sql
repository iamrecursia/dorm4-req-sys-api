-- liquibase formatted sql

-- changeset kozitskiy:002-test-data-users
-- comment: Insert test users data
INSERT INTO users (username, password, user_type)
VALUES
    ('admin1', 'admin1', 'ADMIN'),
    ('st1', 'st1', 'STUDENT'),
    ('pl1', 'pl1', 'PLUMBER'),
    ('el1', 'el1', 'ELECTRICIAN')
    ON CONFLICT (username) DO NOTHING;

-- changeset kozitskiy:002-test-data-requests
-- comment: Insert test requests data
INSERT INTO requests (title, description, request_type, status, student_id, worker_id, created_at, updated_at)
VALUES
    ('Протекает кран', 'В ванной комнате 305 протекает кран, требуется починка', 'PLUMBER', 'PENDING', 2, 3, '2023-01-15 10:00:00', NULL),
    ('Перегорела лампочка', 'Кухня в блоке 407, требуется починка', 'ELECTRICIAN', 'PENDING', 2, 4, '2023-01-15 10:00:00', NULL)
    ON CONFLICT (id) DO NOTHING;