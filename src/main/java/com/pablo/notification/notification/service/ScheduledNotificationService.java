package com.pablo.notification.notification.service;

import com.pablo.notification.notification.domain.NotificationStatus;
import com.pablo.notification.notification.entity.Notification;
import com.pablo.notification.notification.messaging.NotificationProducer;
import com.pablo.notification.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduledNotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationProducer notificationProducer;

    @Scheduled(fixedDelay = 30000)
    @Transactional
    public void processScheduledNotifications() {
        LocalDateTime now = LocalDateTime.now();

        List<Notification> scheduled = notificationRepository.findScheduledNotificationsReadyToSend(now);

        if (scheduled.isEmpty()) {
            return;
        }

        for (Notification notification : scheduled) {
            if (notification.getScheduledAt() != null && notification.getScheduledAt().isAfter(now)) {
                continue;
            }

            notification.setStatus(NotificationStatus.PENDING);
            notificationRepository.save(notification);

            notificationProducer.send(notification.getId());
        }
    }
}
