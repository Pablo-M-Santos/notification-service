package com.pablo.notification.notification.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "telegram")
public record TelegramProperties (
        String botToken,
        String chatId
){
}
