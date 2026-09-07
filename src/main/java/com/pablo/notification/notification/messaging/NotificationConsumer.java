package com.pablo.notification.notification.messaging;

import com.pablo.notification.notification.config.RabbitMQConfig;
import com.pablo.notification.notification.service.NotificationProcessor;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    private static final int MAX_ATTEMPTS = 3;

    private final NotificationProcessor notificationProcessor;
    private final RabbitTemplate rabbitTemplate;

    public NotificationConsumer(
            NotificationProcessor notificationProcessor,
            RabbitTemplate rabbitTemplate
    ) {
        this.notificationProcessor = notificationProcessor;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(
            queues = RabbitMQConfig.NOTIFICATION_QUEUE,
            ackMode = "MANUAL"
    )
    public void consume(
            NotificationMessage message,
            Channel channel,
            @Header("amqp_deliveryTag") long deliveryTag
    ) throws Exception {

        try {

            notificationProcessor.process(
                    message.notificationId(),
                    message.attempt()
            );

        } catch (Exception exception) {

            handleFailure(message);

        } finally {

            channel.basicAck(deliveryTag, false);
        }
    }

    private void handleFailure(NotificationMessage message) {

        if (message.attempt() >= MAX_ATTEMPTS) {
            sendToDlq(message);
            return;
        }

        sendToRetry(message);
    }

    private void sendToRetry(NotificationMessage message) {

        NotificationMessage retryMessage = new NotificationMessage(
                message.notificationId(),
                message.attempt() + 1
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.NOTIFICATION_RETRY_EXCHANGE,
                RabbitMQConfig.NOTIFICATION_RETRY_ROUTING_KEY,
                retryMessage
        );
    }

    private void sendToDlq(NotificationMessage message) {

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.NOTIFICATION_DLQ_EXCHANGE,
                RabbitMQConfig.NOTIFICATION_DLQ_ROUTING_KEY,
                message
        );
    }
}