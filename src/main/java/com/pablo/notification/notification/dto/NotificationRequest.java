package com.pablo.notification.notification.dto;

import com.pablo.notification.notification.domain.NotificationChannel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record NotificationRequest(

        @NotBlank(message = "External ID é obrigatório")
        String externalId,

        @NotBlank(message = "Título é obrigatório")
        String title,

        @NotBlank(message = "Mensagem é obrigatória")
        String message,

        @NotNull(message = "Canal é obrigatório")
        NotificationChannel channel,

        LocalDateTime scheduledAt
) {
}