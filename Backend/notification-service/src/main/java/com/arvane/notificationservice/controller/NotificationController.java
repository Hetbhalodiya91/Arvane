package com.arvane.notificationservice.controller;

import com.arvane.notificationservice.dto.NotificationRequest;
import com.arvane.notificationservice.dto.NotificationResponse;
import com.arvane.notificationservice.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationResponse> sendNotification(@Valid @RequestBody NotificationRequest request) {
        NotificationResponse response = notificationService.sendNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getNotificationById(@PathVariable Long id) {
        NotificationResponse response = notificationService.getNotificationById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/notification-id/{notificationId}")
    public ResponseEntity<NotificationResponse> getNotificationByNotificationId(@PathVariable String notificationId) {
        NotificationResponse response = notificationService.getNotificationByNotificationId(notificationId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getAllNotifications() {
        List<NotificationResponse> responses = notificationService.getAllNotifications();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByStatus(@PathVariable String status) {
        List<NotificationResponse> responses = notificationService.getNotificationsByStatus(status);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/{userEmail}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByUserEmail(@PathVariable String userEmail) {
        List<NotificationResponse> responses = notificationService.getNotificationsByUserEmail(userEmail);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/type-status/{type}/{status}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByTypeAndStatus(
            @PathVariable String type,
            @PathVariable String status) {
        List<NotificationResponse> responses = notificationService.getNotificationsByTypeAndStatus(type, status);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{id}/resend")
    public ResponseEntity<NotificationResponse> resendNotification(@PathVariable Long id) {
        NotificationResponse response = notificationService.resendNotification(id);
        return ResponseEntity.ok(response);
    }
}