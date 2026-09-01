 package com.pablo.notification.notification.service;

import com.pablo.notification.notification.dto.NotificationRequest;
import com.pablo.notification.notification.entity.Notification;
import com.pablo.notification.notification.provider.NotificationProvider;
import com.pablo.notification.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationProvider notificationProvider;
    private final NotificationRepository notificationRepository;

    public void send(NotificationRequest request) {

        Notification notification = Notification.builder()
                .title(request.title())
                .message(request.message())
                .channel(request.channel())
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

        notificationProvider.send(request);

        notification.setStatus("SENT");
        notification.setSentAt(LocalDateTime.now());

        notificationRepository.save(notification);
    }
}