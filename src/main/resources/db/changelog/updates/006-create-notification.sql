CREATE TABLE IF NOT EXISTS notifications (
   id         SERIAL PRIMARY KEY,
   user_id    BIGINT    NOT NULL,
   message    TEXT      NOT NULL,
   is_read    BOOLEAN   NOT NULL DEFAULT FALSE,
   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
   read_at    TIMESTAMP
);