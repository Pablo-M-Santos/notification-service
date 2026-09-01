package com.pablo.notification.notification.controller;

import com.pablo.notification.notification.dto.NotificationRequest;
import com.pablo.notification.notification.dto.NotificationResponse;
import com.pablo.notification.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public NotificationResponse send(
            @Valid @RequestBody NotificationRequest request
    ) {
        return notificationService.send(request);
    }

    @GetMapping("/{id}")
    public NotificationResponse findById(
            @PathVariable Long id
    ) {
        return notificationService.findById(id);
    }
}