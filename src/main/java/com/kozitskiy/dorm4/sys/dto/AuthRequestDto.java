package com.kozitskiy.dorm4.sys.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AuthRequestDto(
        @NotBlank String username,
        @NotBlank String password
) {
}
