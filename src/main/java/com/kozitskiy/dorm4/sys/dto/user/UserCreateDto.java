package com.kozitskiy.dorm4.sys.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UserCreateDto(

        @Schema(description = "username", example = "kirill")
        @NotNull String username,

        @Schema(description = "password", example = "password_56$s+")
        @NotNull String password
) {
}
