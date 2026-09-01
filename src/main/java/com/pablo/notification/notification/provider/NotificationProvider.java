package com.pablo.notification.notification.provider;

import com.pablo.notification.notification.domain.NotificationChannel;
import com.pablo.notification.notification.entity.Notification;

public interface NotificationProvider {

    NotificationChannel getChannel();

    void send(Notification notification);
}