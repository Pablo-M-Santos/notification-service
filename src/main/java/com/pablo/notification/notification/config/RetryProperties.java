package com.pablo.notification.notification.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "notification.retry")
public record RetryProperties(
        int maxAttempts,
        long initialDelay
) {
}