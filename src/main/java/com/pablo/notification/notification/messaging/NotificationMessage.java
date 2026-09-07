package com.pablo.notification.notification.messaging;

public record NotificationMessage(
        Long notificationId,
        int attempt
) {
}