package com.kozitskiy.dorm4.sys.service;

import com.kozitskiy.dorm4.sys.dto.AdminUserCreateDto;
import com.kozitskiy.dorm4.sys.dto.UserCreateDto;
import com.kozitskiy.dorm4.sys.dto.UserResponseDto;
import com.kozitskiy.dorm4.sys.entities.User;
import com.kozitskiy.dorm4.sys.entities.enums.UserType;
import com.kozitskiy.dorm4.sys.mapper.UserMapper;
import com.kozitskiy.dorm4.sys.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    public User createUser(UserCreateDto userCreateDto) {
        User user = User.builder()
                .username(userCreateDto.username())
                .password(passwordEncoder.encode(userCreateDto.password()))
                .userType(UserType.STUDENT)
                .build();
        return userRepository.save(user);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN')")
    public User createUserByAdmin(AdminUserCreateDto adminUserCreateDto) {
        User user = User.builder()
                .username(adminUserCreateDto.username())
                .password(passwordEncoder.encode(adminUserCreateDto.password()))
                .userType(adminUserCreateDto.userType())
                .build();
        return userRepository.save(user);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found"));

        return convertToDto(user);
    }

    @Override
    public List<UserResponseDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    private UserResponseDto convertToDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .userType(user.getUserType())
                .build();
    }

}
