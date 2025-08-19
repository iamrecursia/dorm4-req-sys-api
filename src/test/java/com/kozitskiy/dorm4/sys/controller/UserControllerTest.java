package com.kozitskiy.dorm4.sys.controller;
import com.kozitskiy.dorm4.sys.controllers.UserController;
import com.kozitskiy.dorm4.sys.dto.user.UserCreateDto;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void register_ShouldReturnCreatedUserWithEncodedPassword_WhenValidDtoProvided() {
        // Arrange
        UserCreateDto userCreateDto = new UserCreateDto("testuser", "password123");

        User expectedUser = User.builder()
                .id(1L)
                .username("testuser")
                .password("$2a$10$hashed_password_string") // захешированный пароль
                .userType(UserType.STUDENT)
                .build();

        when(userService.createUser(any(UserCreateDto.class))).thenReturn(expectedUser);

        // Act
        ResponseEntity<User> response = userController.register(userCreateDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        User actualUser = response.getBody();
        assertEquals(1L, actualUser.getId());
        assertEquals("testuser", actualUser.getUsername());
        assertEquals("$2a$10$hashed_password_string", actualUser.getPassword());
        assertEquals(UserType.STUDENT, actualUser.getUserType());

        verify(userService, times(1)).createUser(userCreateDto);
    }

    @Test
    void register_ShouldCallServiceWithCorrectParameters() {
        // Arrange
        UserCreateDto userCreateDto = new UserCreateDto("john_doe", "securePass123!");
        User expectedUser = User.builder()
                .id(2L)
                .username("john_doe")
                .password("hashed_password")
                .userType(UserType.STUDENT)
                .build();

        when(userService.createUser(any(UserCreateDto.class))).thenReturn(expectedUser);

        // Act
        ResponseEntity<User> response = userController.register(userCreateDto);

        // Assert
        verify(userService, times(1)).createUser(userCreateDto);
        assertNotNull(response.getBody());
        assertEquals("john_doe", response.getBody().getUsername());
        assertEquals(UserType.STUDENT, response.getBody().getUserType());
    }
}