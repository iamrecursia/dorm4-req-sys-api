package com.kozitskiy.dorm4.sys.dto;

import com.kozitskiy.dorm4.sys.entities.enums.UserType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AdminUserCreateDto(
        @NotNull String username,
        @NotNull String password,
        @NotNull UserType userType
) {
}
