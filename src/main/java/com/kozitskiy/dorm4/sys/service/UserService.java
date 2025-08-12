package com.kozitskiy.dorm4.sys.service;

import com.kozitskiy.dorm4.sys.dto.AdminUserCreateDto;
import com.kozitskiy.dorm4.sys.dto.UserCreateDto;
import com.kozitskiy.dorm4.sys.dto.UserResponseDto;
import com.kozitskiy.dorm4.sys.entities.User;
import com.kozitskiy.dorm4.sys.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {
    public User createUser(UserCreateDto userCreateDto);
    User createUserByAdmin(AdminUserCreateDto adminUserCreateDto);
    public User findUserByUsername(String username);
    public UserResponseDto getUserById(Long id);
    public List<UserResponseDto> findAllUsers();
    public void deleteUserById(Long id);

}
