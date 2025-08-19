package com.kozitskiy.dorm4.sys.service;

import com.kozitskiy.dorm4.sys.entities.User;
import com.kozitskiy.dorm4.sys.entities.enums.UserType;
import com.kozitskiy.dorm4.sys.exceptions.UserNotFoundException;
import com.kozitskiy.dorm4.sys.repositories.UserRepository;
import com.kozitskiy.dorm4.sys.security.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @Test
    void loadUserByUsername_UserExists_ReturnsUserDetails() {
        // Arrange
        String username = "testuser";
        User user = User.builder()
                .username(username)
                .password("encodedPassword")
                .userType(UserType.ADMIN)
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")));
        assertEquals(1, userDetails.getAuthorities().size());

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void loadUserByUsername_UserWithStudentRole_ReturnsCorrectAuthorities() {
        // Arrange
        String username = "studentuser";
        User user = User.builder()
                .username(username)
                .password("password123")
                .userType(UserType.STUDENT)
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Assert
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("STUDENT")));
        assertFalse(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")));
    }

    @Test
    void loadUserByUsername_UserWithPlumberRole_ReturnsCorrectAuthorities() {
        // Arrange
        String username = "plumberuser";
        User user = User.builder()
                .username(username)
                .password("password123")
                .userType(UserType.PLUMBER)
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Assert
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("PLUMBER")));
    }

    @Test
    void loadUserByUsername_UserNotFound_ThrowsUserNotFoundException() {
        // Arrange
        String username = "nonexistent";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        // Проверяем, что выбрасывается именно ваш кастомный exception
        UserNotFoundException exception = assertThrows(
                UserNotFoundException.class, // Изменили ожидаемый тип исключения
                () -> userDetailsService.loadUserByUsername(username)
        );

        assertEquals("User not found with username: " + username, exception.getMessage());
        verify(userRepository, times(1)).findByUsername(username);
    }
    @Test
    void loadUserByUsername_EmptyUsername_ThrowsUsernameNotFoundException() {
        // Arrange
        String emptyUsername = "";
        when(userRepository.findByUsername(emptyUsername)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(emptyUsername)
        );
    }

    @Test
    void loadUserByUsername_NullUsername_ThrowsUsernameNotFoundException() {
        // Arrange
        when(userRepository.findByUsername(null)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(null)
        );
    }
}
