package com.pablo.notification.notification.messaging;

/**
 * Record que representa uma mensagem de notificação enviada via RabbitMQ.
 * Contém o identificador da notificação e o número da tentativa atual.
 */
public record NotificationMessage(
        Long notificationId,
        int attempt
) {}