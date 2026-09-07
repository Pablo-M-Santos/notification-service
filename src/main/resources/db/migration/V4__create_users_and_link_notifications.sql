CREATE TABLE users
(
    id          BIGSERIAL PRIMARY KEY,
    external_id VARCHAR(255) NOT NULL UNIQUE,
    created_at  TIMESTAMP    NOT NULL
);

ALTER TABLE notifications
    ADD COLUMN user_id BIGINT NOT NULL;

ALTER TABLE notifications
    ADD CONSTRAINT fk_notification_user
        FOREIGN KEY (user_id)
            REFERENCES users (id);

CREATE INDEX idx_notifications_user_id
    ON notifications (user_id);