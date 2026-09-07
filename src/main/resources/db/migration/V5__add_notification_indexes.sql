CREATE INDEX idx_notifications_status
    ON notifications (status);

CREATE INDEX idx_notification_attempts_notification_id
    ON notification_attempts (notification_id);

CREATE INDEX idx_notification_attempts_status
    ON notification_attempts (status);