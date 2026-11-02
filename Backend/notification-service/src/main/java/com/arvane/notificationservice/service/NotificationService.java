package com.arvane.notificationservice.service;

import com.arvane.notificationservice.dto.NotificationRequest;
import com.arvane.notificationservice.dto.NotificationResponse;
import com.arvane.notificationservice.entity.Notification;
import com.arvane.notificationservice.entity.NotificationStatus;
import com.arvane.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationResponse sendNotification(NotificationRequest request) {
        Notification notification = Notification.builder()
                .notificationId(UUID.randomUUID().toString())
                .userEmail(request.getUserEmail())
                .userPhone(request.getUserPhone())
                .type(request.getType())
                .channel(request.getChannel())
                .subject(request.getSubject())
                .message(request.getMessage())
                .status(NotificationStatus.PENDING.name())
                .build();

        Notification saved = notificationRepository.save(notification);

        try {
            sendNotificationInternal(saved);
            saved.setStatus(NotificationStatus.SENT.name());
            saved.setSentAt(LocalDateTime.now());
            log.info("Notification sent successfully: {}", saved.getNotificationId());
        } catch (Exception e) {
            saved.setStatus(NotificationStatus.FAILED.name());
            saved.setErrorMessage(e.getMessage());
            log.error("Failed to send notification: {}", e.getMessage());
        }

        Notification finalNotification = notificationRepository.save(saved);
        return mapToResponse(finalNotification);
    }

    private void sendNotificationInternal(Notification notification) {
        String channel = notification.getChannel().toUpperCase();

        switch (channel) {
            case "EMAIL":
                sendEmail(notification);
                break;
            case "SMS":
                sendSms(notification);
                break;
            default:
                throw new IllegalArgumentException("Unknown channel: " + channel);
        }
    }

    private void sendEmail(Notification notification) {
        log.info("Sending EMAIL to {}: {}", notification.getUserEmail(), notification.getSubject());
        // In production, integrate with SMTP server or email service
        // Currently simulating successful email send
    }

    private void sendSms(Notification notification) {
        log.info("Sending SMS to {}: {}", notification.getUserPhone(), notification.getMessage());
        // In production, integrate with SMS provider (Twilio, AWS SNS, etc.)
        // Currently simulating successful SMS send
    }

    public NotificationResponse getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        return mapToResponse(notification);
    }

    public NotificationResponse getNotificationByNotificationId(String notificationId) {
        Notification notification = notificationRepository.findByNotificationId(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found: " + notificationId));
        return mapToResponse(notification);
    }

    public List<NotificationResponse> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<NotificationResponse> getNotificationsByStatus(String status) {
        return notificationRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<NotificationResponse> getNotificationsByUserEmail(String userEmail) {
        return notificationRepository.findByUserEmail(userEmail).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<NotificationResponse> getNotificationsByTypeAndStatus(String type, String status) {
        return notificationRepository.findByTypeAndStatus(type, status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public NotificationResponse resendNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));

        try {
            notification.setStatus(NotificationStatus.PENDING.name());
            notification.setErrorMessage(null);
            notificationRepository.save(notification);
            return sendNotification(mapRequestToEntity(notification));
        } catch (Exception e) {
            log.error("Failed to resend notification: {}", e.getMessage());
            throw new RuntimeException("Failed to resend notification: " + e.getMessage());
        }
    }

    private NotificationRequest mapRequestToEntity(Notification notification) {
        return NotificationRequest.builder()
                .userEmail(notification.getUserEmail())
                .userPhone(notification.getUserPhone())
                .type(notification.getType())
                .channel(notification.getChannel())
                .subject(notification.getSubject())
                .message(notification.getMessage())
                .build();
    }

    private NotificationResponse mapToResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .notificationId(notification.getNotificationId())
                .userEmail(notification.getUserEmail())
                .userPhone(notification.getUserPhone())
                .type(notification.getType())
                .channel(notification.getChannel())
                .subject(notification.getSubject())
                .message(notification.getMessage())
                .status(NotificationStatus.valueOf(notification.getStatus()))
                .createdAt(notification.getCreatedAt())
                .sentAt(notification.getSentAt())
                .errorMessage(notification.getErrorMessage())
                .success(NotificationStatus.SENT.name().equals(notification.getStatus()))
                .messageText(NotificationStatus.SENT.name().equals(notification.getStatus())
                        ? "Notification sent successfully"
                        : "Notification is " + notification.getStatus().toLowerCase())
                .build();
    }
}