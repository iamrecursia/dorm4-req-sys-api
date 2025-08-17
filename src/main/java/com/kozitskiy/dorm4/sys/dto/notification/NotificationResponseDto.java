package com.kozitskiy.dorm4.sys.dto.notification;

import com.kozitskiy.dorm4.sys.dto.UserResponseDto;
import lombok.Builder;

@Builder
public record NotificationResponseDto(
        String message,
        UserResponseDto userResponseDto

) {
}
