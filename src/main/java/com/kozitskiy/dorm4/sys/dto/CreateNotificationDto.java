package com.kozitskiy.dorm4.sys.dto;

import com.kozitskiy.dorm4.sys.entities.User;

public record CreateNotificationDto(
        String message,
        User user,
        boolean isRead
) {
}
