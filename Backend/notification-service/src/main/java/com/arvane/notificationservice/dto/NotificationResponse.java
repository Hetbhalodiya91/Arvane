package com.arvane.notificationservice.dto;

import com.arvane.notificationservice.entity.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {

    private Long id;
    private String notificationId;
    private String userEmail;
    private String userPhone;
    private String type;
    private String channel;
    private String subject;
    private String message;
    private NotificationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
    private String errorMessage;
    private boolean success;
    private String messageText;
}