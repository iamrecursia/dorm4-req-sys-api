package com.kozitskiy.dorm4.sys.service.user;

import com.kozitskiy.dorm4.sys.dto.auth.AdminUserCreateDto;
import com.kozitskiy.dorm4.sys.dto.user.UserCreateDto;
import com.kozitskiy.dorm4.sys.dto.user.UserResponseDto;
import com.kozitskiy.dorm4.sys.entities.User;

import java.util.List;


public interface UserService {
    User createUser(UserCreateDto userCreateDto);
    User createUserByAdmin(AdminUserCreateDto adminUserCreateDto);
    User findUserByUsername(String username);
    UserResponseDto getUserById(Long id);
    List<UserResponseDto> findAllUsers();
    void deleteUserById(Long id);

}
