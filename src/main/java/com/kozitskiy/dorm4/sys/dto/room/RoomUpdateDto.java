package com.kozitskiy.dorm4.sys.dto.room;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

@Builder
public record RoomUpdateDto(
        @PositiveOrZero(message = "Capacity must be positive or zero")
        Integer capacity,

        @PositiveOrZero(message = "Occupancy must be positive or zero")
        Integer currentOccupancy

        //List<EquipmentUpdateDto> equipment
) { }
