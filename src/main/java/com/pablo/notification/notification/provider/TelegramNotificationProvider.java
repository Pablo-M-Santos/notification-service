package com.pablo.notification.notification.provider;

import com.pablo.notification.notification.client.TelegramClient;
import com.pablo.notification.notification.domain.NotificationChannel;
import com.pablo.notification.notification.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelegramNotificationProvider implements NotificationProvider {

    private final TelegramClient telegramClient;

    @Override
    public NotificationChannel getChannel() {
        return NotificationChannel.TELEGRAM;
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