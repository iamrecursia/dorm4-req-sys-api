package com.kozitskiy.dorm4.sys.dto.room;

import lombok.Builder;

@Builder
public record RoomResponseDto(
        Long id,
        String number,
        Integer capacity,
        Integer currentOccupancy
) {

}
