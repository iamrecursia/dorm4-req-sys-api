package com.kozitskiy.dorm4.sys.mapper;

import com.kozitskiy.dorm4.sys.dto.room.RoomResponseDto;
import com.kozitskiy.dorm4.sys.entities.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {
    public RoomResponseDto convertToDto(Room room) {
        return new RoomResponseDto(
                room.getId(),
                room.getNumber(),
                room.getCapacity(),
                room.getCurrentOccupancy()
        );
    }
}
