package com.pablo.notification.notification.service;

import com.pablo.notification.notification.domain.NotificationStatus;
import com.pablo.notification.notification.entity.Notification;
import com.pablo.notification.notification.entity.NotificationAttempt;
import com.pablo.notification.notification.provider.NotificationProvider;
import com.pablo.notification.notification.provider.NotificationProviderFactory;
import com.pablo.notification.notification.repository.NotificationAttemptRepository;
import com.pablo.notification.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationProcessor {

    private final NotificationRepository notificationRepository;
    private final NotificationAttemptRepository notificationAttemptRepository;
    private final NotificationProviderFactory providerFactory;

    @Transactional
    public void process(Long notificationId, int attemptNumber) {

        Notification notification = findNotification(notificationId);

        NotificationProvider provider =
                providerFactory.getProvider(notification.getChannel());

        NotificationAttempt attempt = createAttempt(
                notification,
                attemptNumber
        );

        try {

            provider.send(notification);

            markAsSent(notification, attempt);

        } catch (Exception exception) {

            markAsFailed(notification, attempt, exception);

            throw exception;
        }
    }

    private Notification findNotification(Long notificationId) {

        return notificationRepository.findById(notificationId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Notification não encontrada: " + notificationId
                        )
                );
    }

    private NotificationAttempt createAttempt(
            Notification notification,
            int attemptNumber
    ) {

        return NotificationAttempt.builder()
                .notification(notification)
                .attemptNumber(attemptNumber)
                .attemptedAt(LocalDateTime.now())
                .build();
    }

    private void markAsSent(
            Notification notification,
            NotificationAttempt attempt
    ) {

        LocalDateTime sentAt = LocalDateTime.now();

        attempt.setStatus(NotificationStatus.SENT);

        notification.setStatus(NotificationStatus.SENT);
        notification.setSentAt(sentAt);
        notification.setErrorMessage(null);

        notificationAttemptRepository.save(attempt);
        notificationRepository.save(notification);
    }

    private void markAsFailed(
            Notification notification,
            NotificationAttempt attempt,
            Exception exception
    ) {

        String errorMessage = exception.getMessage();

        attempt.setStatus(NotificationStatus.FAILED);
        attempt.setErrorMessage(errorMessage);

        notification.setStatus(NotificationStatus.FAILED);
        notification.setErrorMessage(errorMessage);

        notificationAttemptRepository.save(attempt);
        notificationRepository.save(notification);
    }
}