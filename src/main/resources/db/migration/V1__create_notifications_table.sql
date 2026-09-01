CREATE TABLE notifications
(
    id         BIGSERIAL PRIMARY KEY,
    title      VARCHAR(255) NOT NULL,
    message    TEXT         NOT NULL,
    channel    VARCHAR(50)  NOT NULL,
    status     VARCHAR(50)  NOT NULL,
    created_at TIMESTAMP    NOT NULL,
    sent_at    TIMESTAMP
);