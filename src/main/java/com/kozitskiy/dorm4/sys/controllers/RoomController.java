package com.kozitskiy.dorm4.sys.controllers;

import com.kozitskiy.dorm4.sys.dto.RoomRequestDto;
import com.kozitskiy.dorm4.sys.dto.RoomResponseDto;
import com.kozitskiy.dorm4.sys.dto.RoomUpdateDto;
import com.kozitskiy.dorm4.sys.entities.Room;
import com.kozitskiy.dorm4.sys.service.DormitoryService;
import com.kozitskiy.dorm4.sys.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/rooms")
@RequiredArgsConstructor
@Tag(name = "Room Management", description = "API for room management")
public class RoomController {
    private final RoomService roomService;
    private final DormitoryService dormitoryService;

    @PostMapping()
    @Operation(summary = "Create room")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RoomResponseDto> createRoom(@RequestBody @Valid RoomRequestDto room) {
        Room roomEntity = roomService.createRoom(room);

        RoomResponseDto responseDto = RoomResponseDto.builder()
                .currentOccupancy(roomEntity.getCurrentOccupancy())
                .capacity(roomEntity.getCapacity())
                .number(roomEntity.getNumber())
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update room")
    @PutMapping("/{id}")
    public ResponseEntity<RoomResponseDto> updateRoom(@PathVariable Long id,
                                           @RequestBody @Valid RoomUpdateDto dto
    ) {
        Room updatedRoom = roomService.updateRoom(id, dto);

        RoomResponseDto responseDto = RoomResponseDto.builder()
                .id(updatedRoom.getId())
                .number(updatedRoom.getNumber())
                .capacity(dto.capacity())
                .currentOccupancy(dto.currentOccupancy())
                .build();


        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get all rooms")
    @GetMapping("/get-all")
    public ResponseEntity<List<RoomResponseDto>> findAllRooms() {
        return new ResponseEntity<>(roomService.getAllRooms(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete room by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Room> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
