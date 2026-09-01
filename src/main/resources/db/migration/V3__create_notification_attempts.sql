CREATE TABLE notification_attempts
(
    id              BIGSERIAL PRIMARY KEY,
    notification_id BIGINT      NOT NULL,
    attempt_number  INTEGER     NOT NULL,
    status          VARCHAR(20) NOT NULL,
    attempted_at    TIMESTAMP   NOT NULL,
    error_message   TEXT,
    CONSTRAINT fk_notification_attempt
        FOREIGN KEY (notification_id)
            REFERENCES notifications (id)
);

