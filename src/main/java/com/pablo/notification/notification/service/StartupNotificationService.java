package com.pablo.notification.notification.service;

import com.pablo.notification.notification.client.TelegramClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StartupNotificationService {

    private final TelegramClient telegramClient;

    @EventListener(ApplicationReadyEvent.class)
    public void notifyStartup() {

        try {
            String message = """
                    🟢 Notification Service ONLINE
                    
                    ✅ Spring Boot: OK
                    ✅ PostgreSQL: OK
                    ✅ RabbitMQ: OK
                    
                    🚀 Serviço pronto para receber notificações.
                    """;

            telegramClient.sendMessage(message);

            log.info("Notificação de startup enviada para o Telegram.");

        } catch (Exception e) {
            log.error("Falha ao enviar notificação de startup para o Telegram.", e);
        }
    }
}