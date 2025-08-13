package com.kozitskiy.dorm4.sys.service;

import com.kozitskiy.dorm4.sys.dto.RoomRequestDto;
import com.kozitskiy.dorm4.sys.dto.RoomResponseDto;
import com.kozitskiy.dorm4.sys.dto.RoomUpdateDto;
import com.kozitskiy.dorm4.sys.entities.Room;

import java.util.List;

public interface RoomService {
    Room createRoom(RoomRequestDto room);
    Room updateRoom(Long roomId, RoomUpdateDto roomUpdateDto);
    List<RoomResponseDto> getAllRooms();
    void deleteRoom(Long roomId);
}
