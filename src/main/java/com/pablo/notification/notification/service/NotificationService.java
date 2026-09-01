package com.pablo.notification.notification.service;

import com.pablo.notification.notification.dto.NotificationRequest;
import com.pablo.notification.notification.provider.NotificationProvider;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationProvider notificationProvider;

    public NotificationService(NotificationProvider notificationProvider) {
        this.notificationProvider = notificationProvider;
    }

    public NotificationProvider getNotificationProvider() {
        return notificationProvider;
    }

    public void send(NotificationRequest request) {
        notificationProvider.send(request);
    }
}
