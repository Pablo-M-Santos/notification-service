package com.pablo.notification.notification.repository;

import com.pablo.notification.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId")
    List<Notification> findByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT n.* FROM notifications n " +
            "WHERE n.status = 'SCHEDULED' AND n.scheduled_at <= :now",
            nativeQuery = true)
    List<Notification> findScheduledNotificationsReadyToSend(@Param("now") LocalDateTime now);
}
