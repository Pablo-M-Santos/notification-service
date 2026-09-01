package com.pablo.notification.notification.provider;

import com.pablo.notification.notification.client.TelegramClient;
import com.pablo.notification.notification.domain.NotificationChannel;
import com.pablo.notification.notification.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class TelegramNotificationProvider implements NotificationProvider {

    private final TelegramClient telegramClient;

    public TelegramNotificationProvider(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.TELEGRAM;
    }

    @Override
    public void send(Notification notification) {

        String message = """
                🔔 %s

                %s
                """.formatted(
                notification.getTitle(),
                notification.getMessage()
        );

        telegramClient.sendMessage(message);
    }
}