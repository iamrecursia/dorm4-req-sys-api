package com.kozitskiy.dorm4.sys.dto.request;

import com.kozitskiy.dorm4.sys.entities.enums.RequestStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RequestResponseDto(
        Long id,
        String title,
        String description,
        RequestStatus status,
        LocalDateTime createdAt
) {}
