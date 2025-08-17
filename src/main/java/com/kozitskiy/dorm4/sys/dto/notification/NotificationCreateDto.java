package com.kozitskiy.dorm4.sys.dto.notification;

import com.kozitskiy.dorm4.sys.entities.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotificationCreateDto(
        @NotBlank String message,
        @NotNull Long recipientUserId
) {
}
