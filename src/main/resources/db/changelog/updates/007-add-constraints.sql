-- liquibase formatted sql

-- changeset kozitskiy:005-add-constraints
-- comment: Add foreign keys and indexes
-- precondition: tableExists users
-- precondition: tableExists requests
-- precondition: tableExists dormitories
-- precondition: tableExists rooms
-- precondition: tableExists equipment
-- precondition: tableExists notifications

-- For requests table
ALTER TABLE requests
    ADD CONSTRAINT fk_requests_student
        FOREIGN KEY (student_id) REFERENCES users (id)
            ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE requests
    ADD CONSTRAINT fk_requests_worker
        FOREIGN KEY (worker_id) REFERENCES users (id)
            ON DELETE SET NULL ON UPDATE CASCADE;

-- For rooms table (using consistent dormitory_id naming)
ALTER TABLE rooms
    ADD CONSTRAINT fk_rooms_dormitory
        FOREIGN KEY (dormitory_id) REFERENCES dormitories (id)
            ON DELETE CASCADE ON UPDATE CASCADE;

-- For equipment table
ALTER TABLE equipment
    ADD CONSTRAINT fk_equipment_room
        FOREIGN KEY (room_id) REFERENCES rooms (id)
            ON DELETE CASCADE ON UPDATE CASCADE;

-- For notifications table (if it exists)
ALTER TABLE notifications
    ADD CONSTRAINT fk_notifications_user
        FOREIGN KEY (user_id) REFERENCES users (id)
            ON DELETE CASCADE ON UPDATE CASCADE;

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_users_user_type ON users (user_type);
CREATE INDEX IF NOT EXISTS idx_requests_student_id ON requests (student_id);
CREATE INDEX IF NOT EXISTS idx_requests_worker_id ON requests (worker_id);
CREATE INDEX IF NOT EXISTS idx_requests_status ON requests (status);
CREATE INDEX IF NOT EXISTS idx_rooms_dormitory_id ON rooms (dormitory_id);
CREATE INDEX IF NOT EXISTS idx_equipment_room_id ON equipment (room_id);
CREATE INDEX IF NOT EXISTS idx_notifications_user_id ON notifications (user_id);