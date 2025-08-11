package com.kozitskiy.dorm4.sys.dto;

import com.kozitskiy.dorm4.sys.entities.enums.RequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record WorkerRequestDto(
        @NotNull Long id,
        @NotNull String title,
        @NotNull RequestStatus status,
        @NotNull LocalDateTime createdAt
) {
}
