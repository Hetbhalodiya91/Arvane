package com.arvane.notificationservice.repository;

import com.arvane.notificationservice.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findByNotificationId(String notificationId);

    List<Notification> findByStatus(String status);

    List<Notification> findByUserEmail(String userEmail);

    List<Notification> findByTypeAndStatus(String type, String status);
}