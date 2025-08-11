-- Создание таблицы users
CREATE TABLE users
(
    id        SERIAL PRIMARY KEY,
    username  VARCHAR(255) NOT NULL UNIQUE,
    password  VARCHAR(255) NOT NULL,
    user_type VARCHAR(50)  NOT NULL
);

-- Создание таблицы requests
CREATE TABLE requests
(
    id           SERIAL PRIMARY KEY,
    title        VARCHAR(255) NOT NULL,
    description  TEXT,
    request_type VARCHAR(50)  NOT NULL,
    status       VARCHAR(50)  NOT NULL,
    student_id   BIGINT       NOT NULL,
    worker_id    BIGINT,
    created_at   TIMESTAMP    NOT NULL,
    updated_at   TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES users (id),
    FOREIGN KEY (worker_id) REFERENCES users (id)
);

-- Создание индексов для улучшения производительности
CREATE INDEX idx_requests_student_id ON requests (student_id);
CREATE INDEX idx_requests_worker_id ON requests (worker_id);
CREATE INDEX idx_requests_status ON requests (status);