package com.arvane.notificationservice;

import com.arvane.notificationservice.dto.NotificationRequest;
import com.arvane.notificationservice.dto.NotificationResponse;
import com.arvane.notificationservice.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private com.arvane.notificationservice.repository.NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void sendNotification_shouldReturnSentNotification() {
        NotificationRequest request = NotificationRequest.builder()
                .userEmail("test@example.com")
                .type("ORDER")
                .channel("EMAIL")
                .subject("Test Subject")
                .message("Test message content")
                .build();

        com.arvane.notificationservice.entity.Notification savedNotification = com.arvane.notificationservice.entity.Notification.builder()
                .id(1L)
                .notificationId("test-id-123")
                .userEmail("test@example.com")
                .type("ORDER")
                .channel("EMAIL")
                .subject("Test Subject")
                .message("Test message content")
                .status("SENT")
                .build();

        when(notificationRepository.save(any())).thenReturn(savedNotification);

        NotificationResponse response = notificationService.sendNotification(request);

        assertNotNull(response);
        assertEquals("test@example.com", response.getUserEmail());
        assertEquals("ORDER", response.getType());
        assertEquals("SENT", response.getStatus().name());
        assertTrue(response.isSuccess());
    }

    @Test
    void getAllNotifications_shouldReturnListOfNotifications() {
        List<com.arvane.notificationservice.entity.Notification> notifications = Arrays.asList(
                com.arvane.notificationservice.entity.Notification.builder()
                        .id(1L)
                        .notificationId("id-1")
                        .userEmail("user1@example.com")
                        .type("ORDER")
                        .channel("EMAIL")
                        .message("Order placed")
                        .status("SENT")
                        .build(),
                com.arvane.notificationservice.entity.Notification.builder()
                        .id(2L)
                        .notificationId("id-2")
                        .userEmail("user2@example.com")
                        .type("SHIPMENT")
                        .channel("SMS")
                        .message("Shipment update")
                        .status("PENDING")
                        .build()
        );

        when(notificationRepository.findAll()).thenReturn(notifications);

        List<NotificationResponse> responses = notificationService.getAllNotifications();

        assertEquals(2, responses.size());
        assertEquals("user1@example.com", responses.get(0).getUserEmail());
        assertEquals("user2@example.com", responses.get(1).getUserEmail());
    }

    @Test
    void getNotificationsByStatus_shouldReturnFilteredNotifications() {
        List<com.arvane.notificationservice.entity.Notification> notifications = Arrays.asList(
                com.arvane.notificationservice.entity.Notification.builder()
                        .id(1L)
                        .notificationId("id-1")
                        .userEmail("user1@example.com")
                        .type("ORDER")
                        .channel("EMAIL")
                        .message("Order placed")
                        .status("PENDING")
                        .build()
        );

        when(notificationRepository.findByStatus("PENDING")).thenReturn(notifications);

        List<NotificationResponse> responses = notificationService.getNotificationsByStatus("PENDING");

        assertEquals(1, responses.size());
        assertEquals("PENDING", responses.get(0).getStatus().name());
    }
}