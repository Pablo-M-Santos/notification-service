package com.pablo.notification.notification.controller;

import com.pablo.notification.notification.dto.NotificationRequest;
import com.pablo.notification.notification.dto.NotificationResponse;
import com.pablo.notification.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Tag(
        name = "Notificações",
        description = "API para gerenciamento de notificações (envio e consulta)"
)
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "Envia uma nova notificação",
            description = "Cria uma nova notificação para o usuário identificado por `externalId` e a enfileira para envio. "
                    + "O envio real ocorre de forma assíncrona via RabbitMQ.",
            responses = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "Notificação criada e enfileirada com sucesso",
                            content = @Content(
                                    schema = @Schema(implementation = NotificationResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Dados de entrada inválidos"
                    )
            }
    )
    public NotificationResponse send(
            @Valid @RequestBody NotificationRequest request
    ) {
        return notificationService.send(request);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Busca uma notificação por ID",
            description = "Recupera os detalhes de uma notificação específica pelo seu identificador interno.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Notificação encontrada",
                            content = @Content(
                                    schema = @Schema(implementation = NotificationResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Notificação não encontrada"
                    )
            }
    )
    public NotificationResponse findById(
            @PathVariable Long id
    ) {
        return notificationService.findById(id);
    }

    @GetMapping
    @Operation(
            summary = "Lista todas as notificações de um usuário",
            description = "Recupera a lista completa de notificações associadas ao `externalId` informado. "
                    + "Retorna lista vazia caso o usuário não possua notificações.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de notificações do usuário",
                            content = @Content(
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = NotificationResponse.class)
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User não encontrado para o externalId informado"
                    )
            }
    )
    public List<NotificationResponse> findByExternalId(
            @RequestParam String externalId
    ) {
        return notificationService.findByExternalId(externalId);
    }
}