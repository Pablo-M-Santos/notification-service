package com.pablo.notification.notification.dto;

import com.pablo.notification.notification.domain.NotificationChannel;
import com.pablo.notification.notification.domain.NotificationStatus;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        String externalId,
        String title,
        String message,
        NotificationChannel channel,
        NotificationStatus status,
        LocalDateTime createdAt,
        LocalDateTime sentAt
) {
}