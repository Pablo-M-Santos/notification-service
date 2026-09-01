package com.pablo.notification.notification.controller;

import com.pablo.notification.notification.dto.NotificationRequest;
import com.pablo.notification.notification.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<String> sendNotification(
            @RequestBody NotificationRequest request
    ) {
        notificationService.send(request);
        return ResponseEntity.ok("Notificação recebida com sucesso");
    }

}
