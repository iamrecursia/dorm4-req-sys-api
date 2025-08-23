package com.kozitskiy.dorm4.sys.mapper;

import com.kozitskiy.dorm4.sys.dto.room.RoomRequestDto;
import com.kozitskiy.dorm4.sys.dto.room.RoomResponseDto;
import com.kozitskiy.dorm4.sys.dto.room.RoomUpdateDto;
import com.kozitskiy.dorm4.sys.entities.Dormitory;
import com.kozitskiy.dorm4.sys.entities.Room;
import com.kozitskiy.dorm4.sys.exceptions.DormNotFoundException;
import com.kozitskiy.dorm4.sys.repositories.DormitoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class RoomMapper {
    private final DormitoryRepository dormitoryRepository;

    public RoomResponseDto convertToDto(Room room) {
        return  RoomResponseDto.builder()
                .id(room.getId())
                .currentOccupancy(room.getCurrentOccupancy())
                .capacity(room.getCapacity())
                .number(room.getNumber())
                .build();
    }

    public Room convertToEntity(RoomRequestDto roomRequestDto) {
        Dormitory dorm = dormitoryRepository.findById(roomRequestDto.dormitoryId())
                .orElseThrow(() -> new DormNotFoundException(
                        "Dormitory with id = "+ roomRequestDto.dormitoryId() + " not found"));

        return Room.builder()
                .number(roomRequestDto.number())
                .currentOccupancy(roomRequestDto.currentOccupancy())
                .capacity(roomRequestDto.capacity())
                .dormitory(dorm)
                .equipment(new ArrayList<>())
                .build();
    }

    public void updateEntityDormDto(RoomUpdateDto updateDto, Room room) {
        if (updateDto.capacity() != null){
            room.setCapacity(updateDto.capacity());
        }
        if (updateDto.currentOccupancy() != null){
            room.setCurrentOccupancy(updateDto.currentOccupancy());
        }
    }

}
