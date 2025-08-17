package com.kozitskiy.dorm4.sys.dto.auth;

import lombok.Builder;

@Builder
public record AuthResponseDto(
        String token
) {
}
