package com.kozitskiy.dorm4.sys.dto;

import lombok.Builder;

@Builder
public record NotificationResponseDto(
        String message,
        UserResponseDto userResponseDto

) {
}
