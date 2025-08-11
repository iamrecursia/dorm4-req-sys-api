package com.kozitskiy.dorm4.sys.dto;

import com.kozitskiy.dorm4.sys.entities.enums.UserType;
import lombok.Builder;

@Builder
public record UserResponseDto(
        Long id,
        String username,
        UserType userType
) {
}
