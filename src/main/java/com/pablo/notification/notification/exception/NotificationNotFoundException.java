package com.pablo.notification.notification.exception;

public class NotificationNotFoundException extends RuntimeException {

    public NotificationNotFoundException(Long id) {
        super("Notification não encontrada: " + id);
    }
}