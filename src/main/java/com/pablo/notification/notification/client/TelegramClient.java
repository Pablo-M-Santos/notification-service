package com.pablo.notification.notification.client;

import com.pablo.notification.notification.config.TelegramProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class TelegramClient {

    private final RestClient restClient;
    private final TelegramProperties properties;

    public TelegramClient(TelegramProperties properties) {
        this.properties = properties;
        this.restClient = RestClient.builder()
                .baseUrl("https://api.telegram.org")
                .build();
    }

    public void sendMessage(String message) {

        restClient.post()
                .uri("/bot{token}/sendMessage", properties.botToken())
                .body(Map.of(
                        "chat_id", properties.chatId(),
                        "text", message
                ))
                .retrieve()
                .toBodilessEntity();
    }
}