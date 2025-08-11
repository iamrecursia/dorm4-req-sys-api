package com.kozitskiy.dorm4.sys.mapper;

import com.kozitskiy.dorm4.sys.dto.UserCreateDto;
import com.kozitskiy.dorm4.sys.entities.User;
import com.kozitskiy.dorm4.sys.exceptions.UserNotFoundException;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public UserCreateDto toUserCreateDto(User user) {
        if(user == null) {
            throw new UserNotFoundException("User not found");
        }

        return UserCreateDto.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .userType(user.getUserType())
                .build();
    }

    public User toUserEntity(UserCreateDto dto) {
        if(dto == null) {
            throw new UserNotFoundException("User not found");
        }
        return User.builder()
                .username(dto.username())
                .password(dto.password())
                .userType(dto.userType())
                .build();
    }

}
