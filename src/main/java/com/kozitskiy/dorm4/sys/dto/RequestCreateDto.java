package com.kozitskiy.dorm4.sys.dto;

import com.kozitskiy.dorm4.sys.entities.enums.RequestType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record RequestCreateDto(
        @NotNull String title,
        String description,
        @NotNull RequestType requestType,
        @NotNull Long studentId
) {
}
