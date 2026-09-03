package com.pablo.notification.notification.messaging;

import com.pablo.notification.notification.service.NotificationProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    private final NotificationProcessor notificationProcessor;

    public NotificationConsumer(NotificationProcessor notificationProcessor) {
        this.notificationProcessor = notificationProcessor;
    }

    @RabbitListener(queues = "notification.queue")
    public void consume(Long notificationId) {

        notificationProcessor.process(notificationId);
    }
}

