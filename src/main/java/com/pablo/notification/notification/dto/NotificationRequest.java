package com.pablo.notification.notification.dto;

import com.pablo.notification.notification.domain.NotificationChannel;

public record NotificationRequest (
        String title,
        String message,
        NotificationChannel channel
){}
