package com.kozitskiy.dorm4.sys.service;

import com.kozitskiy.dorm4.sys.dto.auth.AdminUserCreateDto;
import com.kozitskiy.dorm4.sys.dto.user.UserCreateDto;
import com.kozitskiy.dorm4.sys.dto.user.UserResponseDto;
import com.kozitskiy.dorm4.sys.entities.User;
import com.kozitskiy.dorm4.sys.entities.enums.UserType;
import com.kozitskiy.dorm4.sys.repositories.UserRepository;
import com.kozitskiy.dorm4.sys.service.user.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private final User testUser = User.builder()
            .id(1L)
            .username("testUser")
            .password("encodedPass")
            .userType(UserType.STUDENT)
            .build();

    private final UserResponseDto testUserDto = UserResponseDto.builder()
            .id(1L)
            .username("testUser")
            .userType(UserType.STUDENT)
            .build();

    @Test
    void createUser_ShouldEncodePasswordAndSaveUser() {
        // Given
        UserCreateDto dto = new UserCreateDto("newUser", "rawPassword");
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User result = userService.createUser(dto);

        // Then
        assertThat(result.getUsername()).isEqualTo("testUser");
        assertThat(result.getPassword()).isEqualTo("encodedPass");
        assertThat(result.getUserType()).isEqualTo(UserType.STUDENT);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void createUserByAdmin_WhenAdmin_ShouldCreateUser() {
        // Given
        AdminUserCreateDto dto = new AdminUserCreateDto("adminUser", "pass", UserType.MANAGER);
        when(passwordEncoder.encode("pass")).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User result = userService.createUserByAdmin(dto);

        // Then
        assertThat(result).isNotNull();
        verify(userRepository).save(any(User.class));
    }

    @Test
    void findUserByUsername_WhenExists_ShouldReturnUser() {
        // Given
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(testUser));

        // When
        User result = userService.findUserByUsername("testUser");

        // Then
        assertThat(result).isEqualTo(testUser);
    }

    @Test
    void findUserByUsername_WhenNotExists_ShouldThrowException() {
        // Given
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> userService.findUserByUsername("unknown"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }

    @Test
    void getUserById_WhenExists_ShouldReturnDto() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        UserResponseDto result = userService.getUserById(1L);

        // Then
        assertThat(result).isEqualTo(testUserDto);
    }

    @Test
    void getUserById_WhenNotExists_ShouldThrowException() {
        // Given
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> userService.getUserById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }

    @Test
    void findAllUsers_ShouldReturnListOfDtos() {
        // Given
        when(userRepository.findAll()).thenReturn(List.of(testUser));

        // When
        List<UserResponseDto> result = userService.findAllUsers();

        // Then
        assertThat(result)
                .hasSize(1)
                .first()
                .usingRecursiveComparison()
                .isEqualTo(testUserDto);
    }

    @Test
    void deleteUserById_ShouldCallRepository() {
        // When
        userService.deleteUserById(1L);

        // Then
        verify(userRepository).deleteById(1L);
    }

}