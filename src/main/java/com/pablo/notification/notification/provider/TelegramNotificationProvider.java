package com.pablo.notification.notification.provider;

import com.pablo.notification.notification.client.TelegramClient;
import com.pablo.notification.notification.dto.NotificationRequest;
import org.springframework.stereotype.Component;

@Component
public class TelegramNotificationProvider implements NotificationProvider {

    private final TelegramClient telegramClient;

    public TelegramNotificationProvider(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }

    @Override
    public void send(NotificationRequest notification) {

        String message = """
                🔔 %s

                %s
                """.formatted(
                notification.title(),
                notification.message()
        );

        telegramClient.sendMessage(message);
    }
}