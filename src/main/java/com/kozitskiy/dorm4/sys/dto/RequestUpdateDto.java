package com.kozitskiy.dorm4.sys.dto;

import com.kozitskiy.dorm4.sys.entities.enums.RequestStatus;
import com.kozitskiy.dorm4.sys.entities.enums.RequestType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record RequestUpdateDto(
        @NotNull(message = "ID cannot be null") Long requestId,
        @NotNull RequestStatus newStatus, // new status
        @NotNull RequestType newRequestType,
        @NotNull Long workerId  // Worker id, that process request
) { // For worker
}
