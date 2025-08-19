package com.kozitskiy.dorm4.sys.service;

import com.kozitskiy.dorm4.sys.dto.notification.NotificationCreateDto;
import com.kozitskiy.dorm4.sys.dto.notification.NotificationResponseDto;
import com.kozitskiy.dorm4.sys.entities.Notification;
import com.kozitskiy.dorm4.sys.entities.User;
import com.kozitskiy.dorm4.sys.entities.enums.UserType;
import com.kozitskiy.dorm4.sys.exceptions.NotificationNotFoundException;
import com.kozitskiy.dorm4.sys.exceptions.UserNotFoundException;
import com.kozitskiy.dorm4.sys.repositories.NotificationRepository;
import com.kozitskiy.dorm4.sys.repositories.UserRepository;
import com.kozitskiy.dorm4.sys.security.service.SecurityService;
import com.kozitskiy.dorm4.sys.service.notification.NotificationServiceImpl;
import com.kozitskiy.dorm4.sys.service.request.RequestAccessService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceImplTest {
    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityService securityService;

    @Mock
    private RequestAccessService requestAccessService;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    void createNotificationForUser_ValidData_ShouldCreateNotification() {
        // Arrange
        Long currentUserId = 1L;
        Long recipientId = 2L;
        NotificationCreateDto dto = new NotificationCreateDto("Test message", recipientId);

        User recipient = User.builder()
                .id(recipientId)
                .username("recipient")
                .userType(UserType.STUDENT)
                .notifications(new ArrayList<>()) // ← ДОБАВЬТЕ ЭТУ СТРОКУ
                .build();

        Notification notification = Notification.builder()
                .id(1L)
                .message("Test message")
                .user(recipient)
                .isRead(false)
                .build();

        when(securityService.getCurrentUserId()).thenReturn(currentUserId);
        doNothing().when(requestAccessService).validateRequestCreationRights(currentUserId);
        when(userRepository.findById(recipientId)).thenReturn(Optional.of(recipient));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        // Act
        NotificationResponseDto result = notificationService.createNotificationForUser(dto);

        // Assert
        assertNotNull(result);
        assertEquals("Test message", result.message());
        assertNotNull(result.userResponseDto());
        assertEquals(recipientId, result.userResponseDto().id());
        assertEquals("recipient", result.userResponseDto().username());

        verify(notificationRepository).save(any(Notification.class));
        verify(userRepository).findById(recipientId);
    }

    @Test
    void createNotificationForUser_EmptyMessage_ShouldThrowIllegalStateException() {
        // Arrange
        NotificationCreateDto dto = new NotificationCreateDto("", 1L);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> notificationService.createNotificationForUser(dto));
    }

    @Test
    void createNotificationForUser_NullMessage_ShouldThrowIllegalStateException() {
        // Arrange
        NotificationCreateDto dto = new NotificationCreateDto(null, 1L);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> notificationService.createNotificationForUser(dto));
    }

    @Test
    void createNotificationForUser_NullRecipientId_ShouldThrowIllegalStateException() {
        // Arrange
        NotificationCreateDto dto = new NotificationCreateDto("Test message", null);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> notificationService.createNotificationForUser(dto));
    }

    @Test
    void createNotificationForUser_RecipientNotFound_ShouldThrowUserNotFoundException() {
        // Arrange
        Long currentUserId = 1L;
        Long recipientId = 2L;
        NotificationCreateDto dto = new NotificationCreateDto("Test message", recipientId);

        when(securityService.getCurrentUserId()).thenReturn(currentUserId);
        doNothing().when(requestAccessService).validateRequestCreationRights(currentUserId);
        when(userRepository.findById(recipientId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class,
                () -> notificationService.createNotificationForUser(dto));
    }

    @Test
    void deleteNotification_ValidId_ShouldDeleteNotification() {
        // Arrange
        Long notificationId = 1L;
        doNothing().when(notificationRepository).deleteById(notificationId);

        // Act
        notificationService.deleteNotification(notificationId);

        // Assert
        verify(notificationRepository).deleteById(notificationId);
    }

    @Test
    void updateNotification_ValidData_ShouldUpdateNotification() {
        // Arrange
        Long notificationId = 1L;
        NotificationCreateDto dto = new NotificationCreateDto("Updated message", 2L);

        Notification existingNotification = Notification.builder()
                .id(notificationId)
                .message("Old message")
                .build();

        Notification updatedNotification = Notification.builder()
                .id(notificationId)
                .message("Updated message")
                .build();

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(existingNotification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(updatedNotification);

        // Act
        Notification result = notificationService.updateNotification(notificationId, dto);

        // Assert
        assertNotNull(result);
        assertEquals("Updated message", result.getMessage());
        verify(notificationRepository).findById(notificationId);
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void updateNotification_NotificationNotFound_ShouldThrowNotificationNotFoundException() {
        // Arrange
        Long notificationId = 1L;
        NotificationCreateDto dto = new NotificationCreateDto("Updated message", 2L);

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotificationNotFoundException.class,
                () -> notificationService.updateNotification(notificationId, dto));
    }

    @Test
    void findAllNotifications_NotificationsExist_ShouldReturnAllNotifications() {
        // Arrange
        User user1 = User.builder()
                .id(1L)
                .username("user1")
                .userType(UserType.STUDENT)
                .build();

        User user2 = User.builder()
                .id(2L)
                .username("user2")
                .userType(UserType.ADMIN)
                .build();

        Notification notification1 = Notification.builder()
                .id(1L)
                .message("Message 1")
                .user(user1)
                .build();

        Notification notification2 = Notification.builder()
                .id(2L)
                .message("Message 2")
                .user(user2)
                .build();

        when(notificationRepository.findAll()).thenReturn(List.of(notification1, notification2));

        // Act
        List<NotificationResponseDto> result = notificationService.findAllNotifications();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("Message 1", result.get(0).message());
        assertEquals(1L, result.get(0).userResponseDto().id());
        assertEquals("user1", result.get(0).userResponseDto().username());

        assertEquals("Message 2", result.get(1).message());
        assertEquals(2L, result.get(1).userResponseDto().id());
        assertEquals("user2", result.get(1).userResponseDto().username());

        verify(notificationRepository).findAll();
    }

    @Test
    void findAllNotifications_NoNotifications_ShouldReturnEmptyList() {
        // Arrange
        when(notificationRepository.findAll()).thenReturn(List.of());

        // Act
        List<NotificationResponseDto> result = notificationService.findAllNotifications();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(notificationRepository).findAll();
    }

    @Test
    void findNotificationById_ValidId_ShouldReturnNotification() {
        // Arrange
        Long notificationId = 1L;
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .userType(UserType.STUDENT)
                .build();

        Notification notification = Notification.builder()
                .id(notificationId)
                .message("Test message")
                .user(user)
                .build();

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));

        // Act
        NotificationResponseDto result = notificationService.findNotificationById(notificationId);

        // Assert
        assertNotNull(result);
        assertEquals("Test message", result.message());
        assertNotNull(result.userResponseDto());
        assertEquals(1L, result.userResponseDto().id());
        assertEquals("testuser", result.userResponseDto().username());
        assertEquals(UserType.STUDENT, result.userResponseDto().userType());

        verify(notificationRepository).findById(notificationId);
    }

    @Test
    void findNotificationById_NotificationNotFound_ShouldThrowNotificationNotFoundException() {
        // Arrange
        Long notificationId = 1L;

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotificationNotFoundException.class,
                () -> notificationService.findNotificationById(notificationId));
    }

    @Test
    void createNotificationForUser_AccessRightsValidationCalled_ShouldValidateAccessRights() {
        // Arrange
        Long currentUserId = 1L;
        Long recipientId = 2L;
        NotificationCreateDto dto = new NotificationCreateDto("Test message", recipientId);

        User recipient = User.builder()
                .id(recipientId)
                .username("recipient")
                .userType(UserType.STUDENT)
                .notifications(new ArrayList<>())
                .build();

        Notification notification = Notification.builder()
                .id(1L)
                .message("Test message")
                .user(recipient)
                .isRead(false)
                .build();

        when(securityService.getCurrentUserId()).thenReturn(currentUserId);
        doNothing().when(requestAccessService).validateRequestCreationRights(currentUserId);
        when(userRepository.findById(recipientId)).thenReturn(Optional.of(recipient));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        // Act
        notificationService.createNotificationForUser(dto);

        // Assert
        verify(requestAccessService).validateRequestCreationRights(currentUserId);
        verify(securityService).getCurrentUserId();
    }
}
