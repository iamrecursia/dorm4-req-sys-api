package com.kozitskiy.dorm4.sys.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

@Builder
public record RoomRequestDto(
        @NotBlank(message = "Room number cannot be empty")
        String number,

        @PositiveOrZero(message = "Capacity must be positive or zero")
        Integer capacity,

        @PositiveOrZero(message = "Occupancy must be positive or zero")
        Integer currentOccupancy,

        @NotNull(message = "Dormitory ID must be specified")
        Long dormitoryId
) {
}
