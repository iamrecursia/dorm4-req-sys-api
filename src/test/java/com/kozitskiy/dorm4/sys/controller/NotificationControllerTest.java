package com.kozitskiy.dorm4.sys.controller;

import com.kozitskiy.dorm4.sys.controllers.NotificationController;
import com.kozitskiy.dorm4.sys.dto.notification.NotificationCreateDto;
import com.kozitskiy.dorm4.sys.dto.notification.NotificationResponseDto;
import com.kozitskiy.dorm4.sys.dto.user.UserResponseDto;
import com.kozitskiy.dorm4.sys.entities.enums.UserType;
import com.kozitskiy.dorm4.sys.service.notification.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    @Test
    void createNotification_ShouldReturnCreatedNotificationWithStatus201_WhenValidDtoProvided() {
        // Arrange
        NotificationCreateDto createDto = new NotificationCreateDto(
                "Test notification message",
                1L
        );

        UserResponseDto userResponseDto = new UserResponseDto(1L, "testuser", UserType.STUDENT);
        NotificationResponseDto expectedResponse = NotificationResponseDto.builder()
                .message("Test notification message")
                .userResponseDto(userResponseDto)
                .build();

        when(notificationService.createNotificationForUser(any(NotificationCreateDto.class))).thenReturn(expectedResponse);

        // Mock authentication for ADMIN
        Authentication auth = new TestingAuthenticationToken(
                "admin",
                null,
                List.of(new SimpleGrantedAuthority("ADMIN"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act
        ResponseEntity<NotificationResponseDto> response = notificationController.createNotification(createDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        NotificationResponseDto actualResponse = response.getBody();
        assertEquals("Test notification message", actualResponse.message());
        assertNotNull(actualResponse.userResponseDto());
        assertEquals(1L, actualResponse.userResponseDto().id());
        assertEquals("testuser", actualResponse.userResponseDto().username());

        verify(notificationService, times(1)).createNotificationForUser(createDto);
    }

    @Test
    void createNotification_ShouldWorkForAllAuthorizedRoles() {
        // Test for ADMIN
        testCreateNotificationForRole("ADMIN");

        // Test for PLUMBER
        testCreateNotificationForRole("PLUMBER");

        // Test for ELECTRICIAN
        testCreateNotificationForRole("ELECTRICIAN");

        // Test for MANAGER
        testCreateNotificationForRole("MANAGER");
    }

    private void testCreateNotificationForRole(String role) {
        // Arrange
        NotificationCreateDto createDto = new NotificationCreateDto(
                "Message from " + role,
                2L
        );

        UserResponseDto userResponseDto = new UserResponseDto(2L, "recipient", UserType.STUDENT);
        NotificationResponseDto expectedResponse = NotificationResponseDto.builder()
                .message("Message from " + role)
                .userResponseDto(userResponseDto)
                .build();

        when(notificationService.createNotificationForUser(any(NotificationCreateDto.class))).thenReturn(expectedResponse);

        // Mock authentication for the role
        Authentication auth = new TestingAuthenticationToken(
                role.toLowerCase(),
                null,
                List.of(new SimpleGrantedAuthority(role))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act
        ResponseEntity<NotificationResponseDto> response = notificationController.createNotification(createDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Message from " + role, response.getBody().message());

        verify(notificationService, times(1)).createNotificationForUser(createDto);

        // Reset mock for next test
        reset(notificationService);
    }

    @Test
    void createNotification_ShouldCallServiceWithCorrectParameters() {
        // Arrange
        NotificationCreateDto createDto = new NotificationCreateDto(
                "Specific message content",
                3L
        );

        UserResponseDto userResponseDto = new UserResponseDto(3L, "specificuser", UserType.ADMIN);
        NotificationResponseDto expectedResponse = NotificationResponseDto.builder()
                .message("Specific message content")
                .userResponseDto(userResponseDto)
                .build();

        when(notificationService.createNotificationForUser(any(NotificationCreateDto.class))).thenReturn(expectedResponse);

        // Mock authentication
        Authentication auth = new TestingAuthenticationToken(
                "admin",
                null,
                List.of(new SimpleGrantedAuthority("ADMIN"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act
        ResponseEntity<NotificationResponseDto> response = notificationController.createNotification(createDto);

        // Assert
        verify(notificationService, times(1)).createNotificationForUser(createDto);
        assertNotNull(response.getBody());
        assertEquals("Specific message content", response.getBody().message());
        assertEquals(3L, createDto.recipientUserId());
    }

    @Test
    void deleteNotificationById_ShouldReturnNoContent_WhenValidIdAndAdminRole() {
        // Arrange
        Long notificationId = 1L;

        // Mock authentication for ADMIN
        Authentication auth = new TestingAuthenticationToken(
                "admin",
                null,
                List.of(new SimpleGrantedAuthority("ADMIN"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act
        ResponseEntity<Void> response = notificationController.deleteNotificationById(notificationId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(notificationService, times(1)).deleteNotification(notificationId);
    }

    @Test
    void deleteNotificationById_ShouldWorkForAllAuthorizedRoles() {
        // Test for PLUMBER
        testDeleteNotificationForRole("PLUMBER");

        // Test for ELECTRICIAN
        testDeleteNotificationForRole("ELECTRICIAN");

        // Test for MANAGER
        testDeleteNotificationForRole("MANAGER");
    }

    private void testDeleteNotificationForRole(String role) {
        // Arrange
        Long notificationId = 2L;

        // Mock authentication for the role
        Authentication auth = new TestingAuthenticationToken(
                role.toLowerCase(),
                null,
                List.of(new SimpleGrantedAuthority(role))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act
        ResponseEntity<Void> response = notificationController.deleteNotificationById(notificationId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(notificationService, times(1)).deleteNotification(notificationId);

        // Reset mock for next test
        reset(notificationService);
    }

    @Test
    void findAllNotifications_ShouldReturnFoundStatus_WhenAdminRole() {
        // Arrange
        UserResponseDto userResponseDto = new UserResponseDto(1L, "testuser", UserType.STUDENT);
        NotificationResponseDto notification1 = NotificationResponseDto.builder()
                .message("Notification 1")
                .userResponseDto(userResponseDto)
                .build();

        NotificationResponseDto notification2 = NotificationResponseDto.builder()
                .message("Notification 2")
                .userResponseDto(userResponseDto)
                .build();

        List<NotificationResponseDto> notifications = Arrays.asList(notification1, notification2);

        when(notificationService.findAllNotifications()).thenReturn(notifications);

        // Mock authentication for ADMIN
        Authentication auth = new TestingAuthenticationToken(
                "admin",
                null,
                List.of(new SimpleGrantedAuthority("ADMIN"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act
        ResponseEntity<NotificationResponseDto> response = notificationController.findAllNotifications();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.FOUND, response.getStatusCode());

        verify(notificationService, times(1)).findAllNotifications();
    }

    @Test
    void findAllNotifications_ShouldWorkForAllAuthorizedRoles() {
        // Test for PLUMBER
        testFindAllNotificationsForRole("PLUMBER");

        // Test for ELECTRICIAN
        testFindAllNotificationsForRole("ELECTRICIAN");

        // Test for MANAGER
        testFindAllNotificationsForRole("MANAGER");
    }

    private void testFindAllNotificationsForRole(String role) {
        // Arrange
        UserResponseDto userResponseDto = new UserResponseDto(1L, "testuser", UserType.STUDENT);
        NotificationResponseDto notification = NotificationResponseDto.builder()
                .message("Test notification")
                .userResponseDto(userResponseDto)
                .build();

        List<NotificationResponseDto> notifications = List.of(notification);

        when(notificationService.findAllNotifications()).thenReturn(notifications);

        // Mock authentication for the role
        Authentication auth = new TestingAuthenticationToken(
                role.toLowerCase(),
                null,
                List.of(new SimpleGrantedAuthority(role))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act
        ResponseEntity<NotificationResponseDto> response = notificationController.findAllNotifications();

        // Assert
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        verify(notificationService, times(1)).findAllNotifications();

        // Reset mock for next test
        reset(notificationService);
    }
}