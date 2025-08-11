package com.kozitskiy.dorm4.sys.service;

import com.kozitskiy.dorm4.sys.dto.UserCreateDto;
import com.kozitskiy.dorm4.sys.dto.UserResponseDto;
import com.kozitskiy.dorm4.sys.entities.User;
import com.kozitskiy.dorm4.sys.entities.enums.UserType;
import com.kozitskiy.dorm4.sys.mapper.UserMapper;
import com.kozitskiy.dorm4.sys.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.el.stream.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public User createUser(UserCreateDto userCreateDto) {
//       User createdUser = User.builder()
//               .username(userCreateDto.username())
//               .password(userCreateDto.password())
//               .userType(userCreateDto.userType())
//               .build();

        return userRepository.save(userMapper.toUserEntity(userCreateDto));
    }

    @Override
    public UserResponseDto findUserByUsername(String username) {
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

    private UserResponseDto convertToDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .userType(user.getUserType())
                .build();
    }

}
