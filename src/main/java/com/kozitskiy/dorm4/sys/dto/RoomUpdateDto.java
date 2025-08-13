package com.kozitskiy.dorm4.sys.dto;

import com.kozitskiy.dorm4.sys.entities.Equipment;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import java.util.List;

@Builder
public record RoomUpdateDto(
        @PositiveOrZero(message = "Capacity must be positive or zero")
        Integer capacity,

        @PositiveOrZero(message = "Occupancy must be positive or zero")
        Integer currentOccupancy

        //List<EquipmentUpdateDto> equipment
) { }
