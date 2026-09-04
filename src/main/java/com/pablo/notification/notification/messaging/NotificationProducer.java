package com.pablo.notification.notification.messaging;

import com.pablo.notification.notification.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Produtor responsável por enviar mensagens de notificação para o RabbitMQ.
 */
@Component
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    public NotificationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Envia uma nova notificação para a fila principal com tentativa inicial 1.
     *
     * @param notificationId identificador único da notificação
     */
    public void send(Long notificationId) {

        NotificationMessage message = new NotificationMessage(
                notificationId,
                1
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.NOTIFICATION_EXCHANGE,
                RabbitMQConfig.NOTIFICATION_ROUTING_KEY,
                message
        );
    }
}