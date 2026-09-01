package com.pablo.notification.notification.provider;

import com.pablo.notification.notification.domain.NotificationChannel;
import com.pablo.notification.notification.dto.NotificationRequest;

public interface NotificationProvider {

    NotificationChannel getChannel();

    void send(NotificationRequest notification);
}