package com.kozitskiy.dorm4.sys.service.room;

import com.kozitskiy.dorm4.sys.dto.room.RoomRequestDto;
import com.kozitskiy.dorm4.sys.dto.room.RoomResponseDto;
import com.kozitskiy.dorm4.sys.dto.room.RoomUpdateDto;
import com.kozitskiy.dorm4.sys.entities.Room;

import java.util.List;

public interface RoomService {
    RoomResponseDto createRoom(RoomRequestDto room);
    RoomResponseDto updateRoom(Long roomId, RoomUpdateDto roomUpdateDto);
    List<RoomResponseDto> getAllRooms();
    void deleteRoom(Long roomId);
}
