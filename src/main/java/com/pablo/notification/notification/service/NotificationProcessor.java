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

import java.time.LocalDateTime;

/**
 * Serviço responsável pelo processamento de notificações.
 * Coordena o envio através do provider, registro de tentativas e atualização de status.
 */
@Service
@RequiredArgsConstructor
public class NotificationProcessor {

    private final NotificationRepository notificationRepository;
    private final NotificationAttemptRepository notificationAttemptRepository;
    private final NotificationProviderFactory providerFactory;

    /**
     * Processa o envio de uma notificação.
     *
     * @param notificationId identificador da notificação
     * @param attemptNumber  número da tentativa atual
     * @throws IllegalArgumentException se a notificação não for encontrada
     */
    public void process(Long notificationId, int attemptNumber) {

        Notification notification =
                notificationRepository.findById(notificationId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Notification não encontrada: " + notificationId
                                )
                        );

        NotificationProvider provider =
                providerFactory.getProvider(notification.getChannel());

        NotificationAttempt attempt = NotificationAttempt.builder()
                .notification(notification)
                .attemptNumber(attemptNumber)
                .attemptedAt(LocalDateTime.now())
                .build();

        try {

            provider.send(notification);

            attempt.setStatus(NotificationStatus.SENT);

            notification.setStatus(NotificationStatus.SENT);
            notification.setSentAt(LocalDateTime.now());

            notificationAttemptRepository.save(attempt);
            notificationRepository.save(notification);

        } catch (Exception exception) {

            attempt.setStatus(NotificationStatus.FAILED);
            attempt.setErrorMessage(exception.getMessage());

            notificationAttemptRepository.save(attempt);

            notification.setStatus(NotificationStatus.FAILED);
            notification.setErrorMessage(exception.getMessage());

            notificationRepository.save(notification);

            throw exception;
        }
    }
}