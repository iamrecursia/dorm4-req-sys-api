package com.kozitskiy.dorm4.sys.dto;

import lombok.Builder;
import lombok.Data;

@Builder
public record RoomResponseDto(
        Long id,
        String number,
        Integer capacity,
        Integer currentOccupancy
) {

}
