package com.kozitskiy.dorm4.sys.controllers;

import com.kozitskiy.dorm4.sys.dto.room.RoomRequestDto;
import com.kozitskiy.dorm4.sys.dto.room.RoomResponseDto;
import com.kozitskiy.dorm4.sys.dto.room.RoomUpdateDto;
import com.kozitskiy.dorm4.sys.entities.Room;
import com.kozitskiy.dorm4.sys.service.dorm.DormitoryService;
import com.kozitskiy.dorm4.sys.service.room.RoomService;
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

    @PostMapping()
    @Operation(summary = "Create room")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public ResponseEntity<RoomResponseDto> createRoom(@RequestBody @Valid RoomRequestDto room) {
            RoomResponseDto roomResponseDto = roomService.createRoom(room);
            return new ResponseEntity<>(roomResponseDto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @Operation(summary = "Update room")
    @PutMapping("/{id}")
    public ResponseEntity<RoomResponseDto> updateRoom(@PathVariable Long id,
                                           @RequestBody @Valid RoomUpdateDto dto
    ) {
        RoomResponseDto roomResponseDto = roomService.updateRoom(id, dto);
        return new ResponseEntity<>(roomResponseDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @Operation(summary = "Get all rooms")
    @GetMapping("/get-all")
    public ResponseEntity<List<RoomResponseDto>> findAllRooms() {
        return new ResponseEntity<>(roomService.getAllRooms(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    @Operation(summary = "Delete room by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Room> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
