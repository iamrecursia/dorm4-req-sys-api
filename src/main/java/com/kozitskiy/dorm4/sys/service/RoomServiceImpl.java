package com.kozitskiy.dorm4.sys.service;

import com.kozitskiy.dorm4.sys.dto.RoomRequestDto;
import com.kozitskiy.dorm4.sys.dto.RoomResponseDto;
import com.kozitskiy.dorm4.sys.dto.RoomUpdateDto;
import com.kozitskiy.dorm4.sys.entities.Dormitory;
import com.kozitskiy.dorm4.sys.entities.Room;
import com.kozitskiy.dorm4.sys.exceptions.DormNotFoundException;
import com.kozitskiy.dorm4.sys.exceptions.RoomNotExistingException;
import com.kozitskiy.dorm4.sys.mapper.RoomMapper;
import com.kozitskiy.dorm4.sys.repositories.DormitoryRepository;
import com.kozitskiy.dorm4.sys.repositories.RoomRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final DormitoryRepository DormitoryRepository;
    private final RoomMapper roomMapper;

    @Override
    @Transactional
    public Room createRoom(@Valid RoomRequestDto roomRequest) {
        Dormitory dormitory = DormitoryRepository.findById(roomRequest.dormitoryId())
                .orElseThrow(() -> new DormNotFoundException("Dormitory with ID " + roomRequest.dormitoryId() + " not found"));

        Room newRoom = Room.builder()
                .number(roomRequest.number())
                .capacity(roomRequest.capacity())
                .currentOccupancy(roomRequest.currentOccupancy() != null ?
                        roomRequest.currentOccupancy() : 0)
                .dormitory(dormitory)
                .build();

        return roomRepository.save(newRoom);
    }

    @Override
    @Transactional
    public Room updateRoom(Long roomId, @Valid RoomUpdateDto roomUpdateDto) {
        Room room = roomRepository.findById(roomId).orElseThrow(
                () -> new RoomNotExistingException("Room with id = " + roomId + "doesn't exist"));

        Room updatedRoom = Room.builder()
                .currentOccupancy(roomUpdateDto.currentOccupancy())
                .capacity(roomUpdateDto.capacity())
                .build();

        // add equipment update
        return roomRepository.save(room);
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
