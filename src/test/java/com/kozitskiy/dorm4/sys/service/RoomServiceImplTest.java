package com.kozitskiy.dorm4.sys.service;

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
import com.kozitskiy.dorm4.sys.service.room.RoomServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoomServiceImplTest {
    @Mock
    private RoomRepository roomRepository;

    @Mock
    private DormitoryRepository dormitoryRepository;

    @Mock
    private RoomMapper roomMapper;

    @InjectMocks
    private RoomServiceImpl roomService;

    @Test
    void createRoom_ShouldReturnRoomResponseDto_WhenDormitoryExists() {
        // Arrange
        Long dormitoryId = 1L;
        RoomRequestDto requestDto = new RoomRequestDto("3", 1, 101, dormitoryId);

        Dormitory dormitory = new Dormitory();
        dormitory.setId(dormitoryId);

        Room savedRoom = Room.builder()
                .id(1L)
                .capacity(requestDto.capacity())
                .currentOccupancy(requestDto.currentOccupancy())
                .number(requestDto.number())
                .dormitory(dormitory)
                .build();

        when(dormitoryRepository.findById(dormitoryId)).thenReturn(Optional.of(dormitory));
        when(roomRepository.save(any(Room.class))).thenReturn(savedRoom);

        // Act
        RoomResponseDto result = roomService.createRoom(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(savedRoom.getCapacity(), result.capacity());
        assertEquals(savedRoom.getCurrentOccupancy(), result.currentOccupancy());
        assertEquals(savedRoom.getNumber(), result.number());

        verify(dormitoryRepository).findById(dormitoryId);
        verify(roomRepository).save(any(Room.class));
    }

    @Test
    void createRoom_ShouldThrowDormNotFoundException_WhenDormitoryNotExists() {
        // Arrange
        Long nonExistentDormitoryId = 999L;
        RoomRequestDto requestDto = new RoomRequestDto("2", 1, 101, nonExistentDormitoryId);

        when(dormitoryRepository.findById(nonExistentDormitoryId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DormNotFoundException.class, () -> roomService.createRoom(requestDto));
        verify(dormitoryRepository).findById(nonExistentDormitoryId);
        verify(roomRepository, never()).save(any(Room.class));
    }

    @Test
    void updateRoom_ShouldReturnUpdatedRoomResponseDto_WhenRoomExists() {
        // Arrange
        Long roomId = 1L;
        RoomUpdateDto updateDto = new RoomUpdateDto(3, 2);

        Room existingRoom = Room.builder()
                .id(roomId)
                .capacity(2)
                .currentOccupancy(1)
                .number("101")
                .build();

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(existingRoom));
        when(roomRepository.save(any(Room.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        RoomResponseDto result = roomService.updateRoom(roomId, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals(roomId, result.id());
        assertEquals(updateDto.capacity(), result.capacity());
        assertEquals(updateDto.currentOccupancy(), result.currentOccupancy());
        assertEquals(existingRoom.getNumber(), result.number());

        verify(roomRepository).findById(roomId);
        verify(roomRepository).save(existingRoom);
    }

    @Test
    void updateRoom_ShouldThrowRoomNotFoundException_WhenRoomNotExists() {
        // Arrange
        Long nonExistentRoomId = 999L;
        RoomUpdateDto updateDto = new RoomUpdateDto(3, 2);

        when(roomRepository.findById(nonExistentRoomId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RoomNotFoundException.class, () -> roomService.updateRoom(nonExistentRoomId, updateDto));
        verify(roomRepository).findById(nonExistentRoomId);
        verify(roomRepository, never()).save(any(Room.class));
    }

    @Test
    void getAllRooms_ShouldReturnListOfRoomResponseDto() {
        // Arrange
        List<Room> rooms = List.of(
                Room.builder().id(1L).number("101").capacity(2).currentOccupancy(1).build(),
                Room.builder().id(2L).number("102").capacity(3).currentOccupancy(2).build()
        );

        List<RoomResponseDto> expectedDtos = List.of(
                new RoomResponseDto(1L, "2", 1, 101),
                new RoomResponseDto(2L, "3", 2, 102)
        );

        when(roomRepository.findAll()).thenReturn(rooms);
        when(roomMapper.convertToDto(rooms.get(0))).thenReturn(expectedDtos.get(0));
        when(roomMapper.convertToDto(rooms.get(1))).thenReturn(expectedDtos.get(1));

        // Act
        List<RoomResponseDto> result = roomService.getAllRooms();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedDtos, result);

        verify(roomRepository).findAll();
        verify(roomMapper, times(2)).convertToDto(any(Room.class));
    }

    @Test
    void getAllRooms_ShouldReturnEmptyList_WhenNoRoomsExist() {
        // Arrange
        when(roomRepository.findAll()).thenReturn(List.of());

        // Act
        List<RoomResponseDto> result = roomService.getAllRooms();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(roomRepository).findAll();
        verify(roomMapper, never()).convertToDto(any(Room.class));
    }

    @Test
    void deleteRoom_ShouldInvokeRepositoryDelete_WhenRoomExists() {
        // Arrange
        Long roomId = 1L;

        roomService.deleteRoom(roomId);

        verify(roomRepository).deleteById(roomId);
        verify(roomRepository, never()).existsById(anyLong());
    }

    @Test
    void deleteRoom_ShouldNotThrowException_WhenRoomNotExists() {
        // Arrange
        Long nonExistentRoomId = 999L;

        doNothing().when(roomRepository).deleteById(nonExistentRoomId);

        // Act & Assert
        assertDoesNotThrow(() -> roomService.deleteRoom(nonExistentRoomId));
        verify(roomRepository).deleteById(nonExistentRoomId);
        verify(roomRepository, never()).existsById(anyLong());
    }

    @Test
    void deleteRoom_ShouldThrowException_WhenRepositoryThrowsException() {
        // Arrange
        Long roomId = 1L;
        doThrow(new RuntimeException("Database error")).when(roomRepository).deleteById(roomId);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> roomService.deleteRoom(roomId));
        verify(roomRepository).deleteById(roomId);
    }
}
