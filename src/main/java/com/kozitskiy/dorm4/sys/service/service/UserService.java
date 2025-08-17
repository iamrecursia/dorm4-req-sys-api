package com.kozitskiy.dorm4.sys.service.service;

import com.kozitskiy.dorm4.sys.dto.auth.AdminUserCreateDto;
import com.kozitskiy.dorm4.sys.dto.user.UserCreateDto;
import com.kozitskiy.dorm4.sys.dto.user.UserResponseDto;
import com.kozitskiy.dorm4.sys.entities.User;

import java.util.List;


public interface UserService {
    public User createUser(UserCreateDto userCreateDto);
    User createUserByAdmin(AdminUserCreateDto adminUserCreateDto);
    public User findUserByUsername(String username);
    public UserResponseDto getUserById(Long id);
    public List<UserResponseDto> findAllUsers();
    public void deleteUserById(Long id);

}
