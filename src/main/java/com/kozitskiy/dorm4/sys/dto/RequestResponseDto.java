package com.kozitskiy.dorm4.sys.dto;

import com.kozitskiy.dorm4.sys.entities.enums.RequestStatus;
import com.kozitskiy.dorm4.sys.entities.enums.RequestType;
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
