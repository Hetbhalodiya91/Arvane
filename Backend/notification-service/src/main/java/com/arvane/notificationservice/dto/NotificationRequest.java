package com.arvane.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {

    @NotNull(message = "User email cannot be null")
    private String userEmail;

    private String userPhone;

    @NotNull(message = "Notification type cannot be null")
    private String type;

    @NotNull(message = "Channel cannot be null")
    private String channel;

    private String subject;

    @NotNull(message = "Message cannot be null")
    @NotBlank(message = "Message cannot be empty")
    private String message;
}