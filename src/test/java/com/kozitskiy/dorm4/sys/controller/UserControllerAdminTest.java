package com.kozitskiy.dorm4.sys.controller;
import com.kozitskiy.dorm4.sys.controllers.UserController;
import com.kozitskiy.dorm4.sys.dto.auth.AdminUserCreateDto;
import com.kozitskiy.dorm4.sys.entities.User;
import com.kozitskiy.dorm4.sys.entities.enums.UserType;
import com.kozitskiy.dorm4.sys.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerAdminTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void createUserByAdmin_ShouldReturnCreatedUserWithStatus201_WhenAdminCreatesUser() {
        // Arrange
        AdminUserCreateDto adminUserCreateDto = new AdminUserCreateDto(
                "admin_created_user",
                "securePassword123!",
                UserType.ADMIN
        );

        User expectedUser = User.builder()
                .id(1L)
                .username("admin_created_user")
                .password("$2a$10$hashed_password")
                .userType(UserType.ADMIN)
                .build();

        when(userService.createUserByAdmin(any(AdminUserCreateDto.class))).thenReturn(expectedUser);

        // Mock admin authentication
        Authentication auth = new TestingAuthenticationToken(
                "admin",
                null,
                List.of(new SimpleGrantedAuthority("ADMIN"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act
        ResponseEntity<User> response = userController.createUser(adminUserCreateDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        User actualUser = response.getBody();
        assertEquals(1L, actualUser.getId());
        assertEquals("admin_created_user", actualUser.getUsername());
        assertEquals("$2a$10$hashed_password", actualUser.getPassword());
        assertEquals(UserType.ADMIN, actualUser.getUserType());

        verify(userService, times(1)).createUserByAdmin(adminUserCreateDto);
    }

    @Test
    void createUserByAdmin_ShouldCreateUserWithDifferentUserTypes() {
        // Arrange
        AdminUserCreateDto adminUserCreateDto = new AdminUserCreateDto(
                "student_user",
                "password123",
                UserType.STUDENT
        );

        User expectedUser = User.builder()
                .id(2L)
                .username("student_user")
                .password("hashed_password")
                .userType(UserType.STUDENT)
                .build();

        when(userService.createUserByAdmin(any(AdminUserCreateDto.class))).thenReturn(expectedUser);

        // Mock admin authentication
        Authentication auth = new TestingAuthenticationToken(
                "admin",
                null,
                List.of(new SimpleGrantedAuthority("ADMIN"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act
        ResponseEntity<User> response = userController.createUser(adminUserCreateDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(UserType.STUDENT, response.getBody().getUserType());

        verify(userService, times(1)).createUserByAdmin(adminUserCreateDto);
    }

    @Test
    void createUserByAdmin_ShouldCallServiceWithCorrectParameters() {
        // Arrange
        AdminUserCreateDto adminUserCreateDto = new AdminUserCreateDto(
                "test_user",
                "test_password",
                UserType.MANAGER
        );

        User expectedUser = User.builder()
                .id(3L)
                .username("test_user")
                .password("hashed_test_password")
                .userType(UserType.MANAGER)
                .build();

        when(userService.createUserByAdmin(any(AdminUserCreateDto.class))).thenReturn(expectedUser);

        // Mock admin authentication
        Authentication auth = new TestingAuthenticationToken(
                "admin",
                null,
                List.of(new SimpleGrantedAuthority("ADMIN"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act
        ResponseEntity<User> response = userController.createUser(adminUserCreateDto);

        // Assert
        verify(userService, times(1)).createUserByAdmin(adminUserCreateDto);
        assertNotNull(response.getBody());
        assertEquals("test_user", response.getBody().getUsername());
        assertEquals(UserType.MANAGER, response.getBody().getUserType());
    }
}