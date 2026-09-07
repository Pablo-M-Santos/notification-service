package com.pablo.notification.notification.service;

import com.pablo.notification.notification.domain.NotificationStatus;
import com.pablo.notification.notification.dto.NotificationRequest;
import com.pablo.notification.notification.dto.NotificationResponse;
import com.pablo.notification.notification.entity.Notification;
import com.pablo.notification.notification.entity.User;
import com.pablo.notification.notification.exception.NotificationNotFoundException;
import com.pablo.notification.notification.messaging.NotificationProducer;
import com.pablo.notification.notification.provider.NotificationProviderFactory;
import com.pablo.notification.notification.repository.NotificationRepository;
import com.pablo.notification.notification.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationProducer notificationProducer;
    private final NotificationProviderFactory providerFactory;

    @Transactional
    public NotificationResponse send(NotificationRequest request) {

        providerFactory.getProvider(request.channel());

        User user = findOrCreateUser(request.externalId());

        Notification notification = Notification.builder()
                .user(user)
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

    private User findOrCreateUser(String externalId) {

        return userRepository.findByExternalId(externalId)
                .orElseGet(() -> createUser(externalId));
    }

    private User createUser(String externalId) {

        try {

            return userRepository.save(
                    User.builder()
                            .externalId(externalId)
                            .createdAt(LocalDateTime.now())
                            .build()
            );

        } catch (DataIntegrityViolationException exception) {

            return userRepository.findByExternalId(externalId)
                    .orElseThrow(() -> exception);
        }
    }

    public NotificationResponse findById(Long id) {

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(id));

        return toResponse(notification);
    }

    private NotificationResponse toResponse(Notification notification) {

        return new NotificationResponse(
                notification.getId(),
                notification.getUser().getExternalId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getChannel(),
                notification.getStatus(),
                notification.getCreatedAt(),
                notification.getSentAt()
        );
    }
}