INSERT INTO users (username, password, user_type)
VALUES
    ('admin1', 'admin1', 'ADMIN'),
    ('st1', 'st1', 'STUDENT'),
    ('pl1', 'pl1', 'PLUMBER'),
    ('el1', 'el1', 'ELECTRICIAN');

INSERT INTO requests (id, title, description, request_type, status, student_id, worker_id, created_at, updated_at)
VALUES (1, 'Протекает кран', 'В ванной комнате 305 протекает кран, требуется починка', 'PLUMBER',
        'PENDING', 2, 3, '2023-01-15 10:00:00', null),
       (1, 'Перегорела лампочка', 'Кухня в блоке 407, требуется починка', 'ELECTRICIAN',
        'PENDING', 2, 4, '2023-01-15 10:00:00', null)

