package com.pablo.notification.notification.service;

import com.pablo.notification.notification.domain.NotificationStatus;
import com.pablo.notification.notification.entity.Notification;
import com.pablo.notification.notification.messaging.NotificationProducer;
import com.pablo.notification.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
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

        log.info("Processando {} notificações agendadas.", scheduled.size());

        for (Notification notification : scheduled) {
            if (notification.getScheduledAt() != null && notification.getScheduledAt().isAfter(now)) {
                log.warn("Notificação {} agendada para {} mas o horário atual é {}. Pulando envio antecipado.",
                        notification.getId(), notification.getScheduledAt(), now);
                continue;
            }

            notification.setStatus(NotificationStatus.PENDING);
            notificationRepository.save(notification);

            notificationProducer.send(notification.getId());

            log.info("Notificação agendada enviada para processamento: id={}", notification.getId());
        }
    }
}
