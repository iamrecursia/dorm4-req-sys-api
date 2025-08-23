package com.kozitskiy.dorm4.sys.service.room;

import com.kozitskiy.dorm4.sys.dto.room.RoomRequestDto;
import com.kozitskiy.dorm4.sys.dto.room.RoomResponseDto;
import com.kozitskiy.dorm4.sys.dto.room.RoomUpdateDto;
import com.kozitskiy.dorm4.sys.entities.Dormitory;
import com.kozitskiy.dorm4.sys.entities.Room;
import com.kozitskiy.dorm4.sys.exceptions.DormNotFoundException;
import com.kozitskiy.dorm4.sys.exceptions.RoomNotFoundException;
import com.kozitskiy.dorm4.sys.mapper.RoomMapper;
import com.kozitskiy.dorm4.sys.repositories.DormitoryRepository;
import com.kozitskiy.dorm4.sys.repositories.RoomRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final DormitoryRepository DormitoryRepository;
    private final RoomMapper roomMapper;

    @Override
    @Transactional
    public RoomResponseDto createRoom(@Valid RoomRequestDto requestDto) {
        Room createdRoom = roomMapper.convertToEntity(requestDto);

        roomRepository.save(createdRoom);

        return roomMapper.convertToDto(createdRoom);
    }

    @Override
    @Transactional
    public RoomResponseDto updateRoom(Long roomId, @Valid RoomUpdateDto roomUpdateDto) {
        Room room = roomRepository.findById(roomId).orElseThrow(
                () -> new RoomNotFoundException("Room with id = " + roomId + "doesn't exist"));

        roomMapper.updateEntityDormDto(roomUpdateDto, room);
        Room updatedRoom = roomRepository.save(room);

        return roomMapper.convertToDto(updatedRoom);
    }

    @Override
    public List<RoomResponseDto> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return rooms.stream()
                .map(roomMapper::convertToDto)
                .toList();
    }

    @Override
    public void deleteRoom(Long roomId) {
        roomRepository.deleteById(roomId);
    }

}
