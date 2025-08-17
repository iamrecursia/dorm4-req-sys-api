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
        Dormitory dormitory = DormitoryRepository.findById(requestDto.dormitoryId())
                .orElseThrow(() -> new DormNotFoundException("Dormitory with ID " + requestDto.dormitoryId() + " not found"));

        Room createdRoom = Room.builder()
                .capacity(requestDto.capacity())
                .dormitory(dormitory)
                .currentOccupancy(requestDto.currentOccupancy())
                .number(requestDto.number())
                .equipment(new ArrayList<>())
                .build();
        roomRepository.save(createdRoom);

        return RoomResponseDto.builder()
                .id(createdRoom.getId())
                .capacity(createdRoom.getCapacity())
                .currentOccupancy(createdRoom.getCurrentOccupancy())
                .number(createdRoom.getNumber())
                .build();
    }

    @Override
    @Transactional
    public RoomResponseDto updateRoom(Long roomId, @Valid RoomUpdateDto roomUpdateDto) {
        Room room = roomRepository.findById(roomId).orElseThrow(
                () -> new RoomNotFoundException("Room with id = " + roomId + "doesn't exist"));

        room.setCapacity(roomUpdateDto.capacity());
        room.setCurrentOccupancy(roomUpdateDto.currentOccupancy());

        roomRepository.save(room);
        // add equipment update
        return RoomResponseDto.builder()
                .currentOccupancy(room.getCurrentOccupancy())
                .number(room.getNumber())
                .id(room.getId())
                .capacity(room.getCapacity())
                .build();
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
