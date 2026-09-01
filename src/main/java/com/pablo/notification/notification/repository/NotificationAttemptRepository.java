package com.pablo.notification.notification.repository;

import com.pablo.notification.notification.entity.NotificationAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationAttemptRepository
        extends JpaRepository<NotificationAttempt, Long> {
}
