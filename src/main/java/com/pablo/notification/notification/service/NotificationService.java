package com.pablo.notification.notification.service;

import com.pablo.notification.notification.domain.NotificationStatus;
import com.pablo.notification.notification.dto.NotificationRequest;
import com.pablo.notification.notification.dto.NotificationResponse;
import com.pablo.notification.notification.entity.Notification;
import com.pablo.notification.notification.exception.NotificationNotFoundException;
import com.pablo.notification.notification.messaging.NotificationProducer;
import com.pablo.notification.notification.provider.NotificationProviderFactory;
import com.pablo.notification.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationProducer notificationProducer;
    private final NotificationProviderFactory providerFactory;

    public NotificationResponse send(NotificationRequest request) {

        providerFactory.getProvider(request.channel());

        Notification notification = Notification.builder()
                .title(request.title())
                .message(request.message())
                .channel(request.channel())
                .status(NotificationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

        notificationProducer.send(notification.getId());

        return toResponse(notification);
    }

    public NotificationResponse findById(Long id) {

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(id));

        return toResponse(notification);
    }

    private NotificationResponse toResponse(Notification notification) {

        return new NotificationResponse(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getChannel(),
                notification.getStatus(),
                notification.getCreatedAt(),
                notification.getSentAt()
        );
    }
}