package com.kozitskiy.dorm4.sys.service;

import com.kozitskiy.dorm4.sys.entities.User;
import com.kozitskiy.dorm4.sys.entities.enums.UserType;
import com.kozitskiy.dorm4.sys.exceptions.UserNotFoundException;
import com.kozitskiy.dorm4.sys.repositories.UserRepository;
import com.kozitskiy.dorm4.sys.security.service.SecurityService;
import com.kozitskiy.dorm4.sys.service.request.RequestAccessService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class RequestAccessServiceTest {
    @Mock
    private SecurityService securityService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RequestAccessService requestAccessService;

    @Test
    void validateRequestCreationRights_AdminCreatingForStudent_ShouldNotThrowException() {
        // Arrange
        Long userId = 1L;
        User student = User.builder()
                .id(userId)
                .userType(UserType.STUDENT)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(student));
        when(securityService.isCurrentUserAdmin()).thenReturn(true);

        // Act & Assert
        assertDoesNotThrow(() -> requestAccessService.validateRequestCreationRights(userId));

        verify(userRepository).findById(userId);
        verify(securityService).isCurrentUserAdmin();
    }

    @Test
    void validateRequestCreationRights_StudentCreatingForSelf_ShouldNotThrowException() {
        // Arrange
        Long userId = 1L;
        User student = User.builder()
                .id(userId)
                .userType(UserType.STUDENT)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(student));
        when(securityService.isCurrentUserAdmin()).thenReturn(false);

        // Act & Assert
        assertDoesNotThrow(() -> requestAccessService.validateRequestCreationRights(userId));
    }

    @Test
    void validateRequestCreationRights_UserNotFound_ShouldThrowUserNotFoundException() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class,
                () -> requestAccessService.validateRequestCreationRights(userId));
    }

    @Test
    void validateRequestCreationRights_NonAdminCreatingForNonStudent_ShouldThrowAccessDenied() {
        // Arrange
        Long userId = 1L;
        User worker = User.builder()
                .id(userId)
                .userType(UserType.ELECTRICIAN)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(worker));
        when(securityService.isCurrentUserAdmin()).thenReturn(false);

        // Act & Assert
        assertThrows(AccessDeniedException.class,
                () -> requestAccessService.validateRequestCreationRights(userId));
    }

    @Test
    void validateRequestCreationRights_AdminCreatingForNonStudent_ShouldThrowAccessDenied() {
        // Arrange
        Long userId = 1L;
        User worker = User.builder()
                .id(userId)
                .userType(UserType.ELECTRICIAN)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(worker));
        when(securityService.isCurrentUserAdmin()).thenReturn(true);

        // Act & Assert
        assertThrows(AccessDeniedException.class,
                () -> requestAccessService.validateRequestCreationRights(userId));
    }
}
