package com.kozitskiy.dorm4.sys.service;

import com.kozitskiy.dorm4.sys.dto.auth.AuthRequestDto;
import com.kozitskiy.dorm4.sys.dto.auth.AuthResponseDto;
import com.kozitskiy.dorm4.sys.entities.User;
import com.kozitskiy.dorm4.sys.repositories.UserRepository;
import com.kozitskiy.dorm4.sys.security.service.AuthService;
import com.kozitskiy.dorm4.sys.security.utils.JwtTokenProvider;
import com.kozitskiy.dorm4.sys.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    void authenticate_ValidCredentials_ShouldReturnAuthResponse() {
        // Arrange
        AuthRequestDto authRequestDto = new AuthRequestDto("testuser", "password123");

        Authentication authentication = mock(Authentication.class);
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("encodedPassword")
                .build();

        String token = "jwt-token-123";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userService.findUserByUsername("testuser")).thenReturn(user);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn(token);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        AuthResponseDto result = authService.authenticate(authRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals("jwt-token-123", result.token());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService).findUserByUsername("testuser");
        verify(jwtTokenProvider).generateToken(authentication);
        verify(userRepository).save(user);
    }

    @Test
    void authenticate_InvalidCredentials_ShouldThrowBadCredentialsException() {
        // Arrange
        AuthRequestDto authRequestDto = new AuthRequestDto("wronguser", "wrongpass");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act & Assert
        assertThrows(BadCredentialsException.class,
                () -> authService.authenticate(authRequestDto));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService, never()).findUserByUsername(anyString());
        verify(jwtTokenProvider, never()).generateToken(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void authenticate_UserNotFound_ShouldThrowException() {
        // Arrange
        AuthRequestDto authRequestDto = new AuthRequestDto("nonexistent", "password123");

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userService.findUserByUsername("nonexistent"))
                .thenThrow(new RuntimeException("User not found"));

        // Act & Assert
        assertThrows(RuntimeException.class,
                () -> authService.authenticate(authRequestDto));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService).findUserByUsername("nonexistent");
        verify(jwtTokenProvider, never()).generateToken(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void authenticate_TokenGenerationFails_ShouldThrowException() {
        // Arrange
        AuthRequestDto authRequestDto = new AuthRequestDto("testuser", "password123");

        Authentication authentication = mock(Authentication.class);
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("encodedPassword")
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userService.findUserByUsername("testuser")).thenReturn(user);
        when(jwtTokenProvider.generateToken(authentication))
                .thenThrow(new RuntimeException("Token generation failed"));

        // Act & Assert
        assertThrows(RuntimeException.class,
                () -> authService.authenticate(authRequestDto));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService).findUserByUsername("testuser");
        verify(jwtTokenProvider).generateToken(authentication);
        verify(userRepository, never()).save(any());
    }

    @Test
    void authenticate_UserSaveFails_ShouldThrowException() {
        // Arrange
        AuthRequestDto authRequestDto = new AuthRequestDto("testuser", "password123");

        Authentication authentication = mock(Authentication.class);
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("encodedPassword")
                .build();

        String token = "jwt-token-123";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userService.findUserByUsername("testuser")).thenReturn(user);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn(token);
        when(userRepository.save(any(User.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class,
                () -> authService.authenticate(authRequestDto));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService).findUserByUsername("testuser");
        verify(jwtTokenProvider).generateToken(authentication);
        verify(userRepository).save(user);
    }

    @Test
    void authenticate_ShouldSetTokenToUser() {
        // Arrange
        AuthRequestDto authRequestDto = new AuthRequestDto("testuser", "password123");

        Authentication authentication = mock(Authentication.class);
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("encodedPassword")
                .token(null)
                .build();

        String token = "jwt-token-123";
        User savedUser = User.builder()
                .id(1L)
                .username("testuser")
                .password("encodedPassword")
                .token("jwt-token-123")
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userService.findUserByUsername("testuser")).thenReturn(user);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn(token);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        AuthResponseDto result = authService.authenticate(authRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals("jwt-token-123", result.token());

        // Проверяем, что токен установлен в пользователе перед сохранением
        verify(userRepository).save(argThat(userArg ->
                userArg.getToken().equals("jwt-token-123")));
    }

    @Test
    void authenticate_NullUsername_ShouldThrowException() {
        // Arrange
        AuthRequestDto authRequestDto = new AuthRequestDto(null, "password123");

        // Act & Assert
        assertThrows(Exception.class,
                () -> authService.authenticate(authRequestDto));
    }

    @Test
    void authenticate_NullPassword_ShouldThrowException() {
        // Arrange
        AuthRequestDto authRequestDto = new AuthRequestDto("testuser", null);

        // Act & Assert
        assertThrows(Exception.class,
                () -> authService.authenticate(authRequestDto));
    }

    @Test
    void authenticate_EmptyUsername_ShouldThrowException() {
        // Arrange
        AuthRequestDto authRequestDto = new AuthRequestDto("", "password123");

        // Act & Assert
        assertThrows(Exception.class,
                () -> authService.authenticate(authRequestDto));
    }

    @Test
    void authenticate_EmptyPassword_ShouldThrowException() {
        // Arrange
        AuthRequestDto authRequestDto = new AuthRequestDto("testuser", "");

        // Act & Assert
        assertThrows(Exception.class,
                () -> authService.authenticate(authRequestDto));
    }
}
