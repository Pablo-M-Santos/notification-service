package com.pablo.notification.notification.provider;

import com.pablo.notification.notification.dto.NotificationRequest;

public interface NotificationProvider {
    void send(NotificationRequest notification);
}
