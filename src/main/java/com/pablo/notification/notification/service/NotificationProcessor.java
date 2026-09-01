package com.pablo.notification.notification.service;

import com.pablo.notification.notification.config.RetryProperties;
import com.pablo.notification.notification.domain.NotificationStatus;
import com.pablo.notification.notification.dto.NotificationRequest;
import com.pablo.notification.notification.entity.Notification;
import com.pablo.notification.notification.entity.NotificationAttempt;
import com.pablo.notification.notification.provider.NotificationProvider;
import com.pablo.notification.notification.provider.NotificationProviderFactory;
import com.pablo.notification.notification.repository.NotificationAttemptRepository;
import com.pablo.notification.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationProcessor {

    private final NotificationProviderFactory providerFactory;
    private final NotificationRepository notificationRepository;
    private final NotificationAttemptRepository notificationAttemptRepository;
    private final RetryProperties retryProperties;

    @Async("notificationExecutor")
    public void process(
            Notification notification,
            NotificationRequest request
    ) {

        NotificationProvider provider =
                providerFactory.getProvider(request.channel());

        for (int attemptNumber = 1;
             attemptNumber <= retryProperties.maxAttempts();
             attemptNumber++) {

            NotificationAttempt attempt = NotificationAttempt.builder()
                    .notification(notification)
                    .attemptNumber(attemptNumber)
                    .attemptedAt(LocalDateTime.now())
                    .build();

            try {

                provider.send(request);

                attempt.setStatus(NotificationStatus.SENT);

                notification.setStatus(NotificationStatus.SENT);
                notification.setSentAt(LocalDateTime.now());

                notificationAttemptRepository.save(attempt);
                notificationRepository.save(notification);

                return;

            } catch (Exception exception) {

                attempt.setStatus(NotificationStatus.FAILED);
                attempt.setErrorMessage(exception.getMessage());

                notificationAttemptRepository.save(attempt);

                notification.setStatus(NotificationStatus.FAILED);
                notification.setErrorMessage(exception.getMessage());

                if (attemptNumber < retryProperties.maxAttempts()) {
                    waitBeforeRetry(attemptNumber);
                }
            }
        }

        notificationRepository.save(notification);
    }

    private void waitBeforeRetry(int attemptNumber) {

        long delay = retryProperties.initialDelay()
                * (long) Math.pow(2, attemptNumber - 1);

        try {

            Thread.sleep(delay);

        } catch (InterruptedException exception) {

            Thread.currentThread().interrupt();

            throw new IllegalStateException(
                    "Thread interrompida durante o retry",
                    exception
            );
        }
    }
}