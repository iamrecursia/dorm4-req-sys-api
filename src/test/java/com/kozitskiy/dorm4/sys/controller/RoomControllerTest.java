package com.kozitskiy.dorm4.sys.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kozitskiy.dorm4.sys.controllers.RoomController;
import com.kozitskiy.dorm4.sys.dto.room.RoomRequestDto;
import com.kozitskiy.dorm4.sys.dto.room.RoomResponseDto;
import com.kozitskiy.dorm4.sys.dto.room.RoomUpdateDto;
import com.kozitskiy.dorm4.sys.service.room.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RoomControllerTest {

    @Mock
    private RoomService roomService;

    @InjectMocks
    private RoomController roomController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private RoomRequestDto roomRequestDto;
    private RoomResponseDto roomResponseDto;
    private RoomUpdateDto roomUpdateDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(roomController).build();
        objectMapper = new ObjectMapper();

        // Initialize test data using builder pattern
        roomRequestDto = RoomRequestDto.builder()
                .number("101")
                .capacity(2)
                .currentOccupancy(0)
                .dormitoryId(1L)
                .build();

        roomResponseDto = RoomResponseDto.builder()
                .id(1L)
                .number("101")
                .capacity(2)
                .currentOccupancy(0)
                .build();

        roomUpdateDto = RoomUpdateDto.builder()
                .capacity(3)
                .currentOccupancy(1)
                .build();
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void createRoom_WithValidData_ShouldReturnCreated() throws Exception {
        when(roomService.createRoom(any(RoomRequestDto.class))).thenReturn(roomResponseDto);

        mockMvc.perform(post("/api/v1/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roomRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.number").value("101"))  // Изменено с roomNumber на number
                .andExpect(jsonPath("$.capacity").value(2))
                .andExpect(jsonPath("$.currentOccupancy").value(0));

        verify(roomService, times(1)).createRoom(any(RoomRequestDto.class));
    }

    @Test
    @WithMockUser(authorities = {"MANAGER"})
    void createRoom_WithManagerAuthority_ShouldReturnCreated() throws Exception {
        when(roomService.createRoom(any(RoomRequestDto.class))).thenReturn(roomResponseDto);

        mockMvc.perform(post("/api/v1/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roomRequestDto)))
                .andExpect(status().isCreated());

        verify(roomService, times(1)).createRoom(any(RoomRequestDto.class));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void createRoom_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Создаем невалидный DTO (без обязательных полей)
        RoomRequestDto invalidDto = RoomRequestDto.builder()
                .capacity(2)
                .currentOccupancy(0)
                .build();

        mockMvc.perform(post("/api/v1/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

        verify(roomService, never()).createRoom(any(RoomRequestDto.class));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void updateRoom_WithValidData_ShouldReturnOk() throws Exception {
        when(roomService.updateRoom(eq(1L), any(RoomUpdateDto.class))).thenReturn(roomResponseDto);

        mockMvc.perform(put("/api/v1/rooms/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roomUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.number").value("101"))  // Изменено с roomNumber на number
                .andExpect(jsonPath("$.capacity").value(2));

        verify(roomService, times(1)).updateRoom(eq(1L), any(RoomUpdateDto.class));
    }

    @Test
    @WithMockUser(authorities = {"MANAGER"})
    void updateRoom_WithManagerAuthority_ShouldReturnOk() throws Exception {
        when(roomService.updateRoom(eq(1L), any(RoomUpdateDto.class))).thenReturn(roomResponseDto);

        mockMvc.perform(put("/api/v1/rooms/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roomUpdateDto)))
                .andExpect(status().isOk());

        verify(roomService, times(1)).updateRoom(eq(1L), any(RoomUpdateDto.class));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void updateRoom_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        //невалидный DTO
        RoomUpdateDto invalidDto = RoomUpdateDto.builder()
                .capacity(-1)
                .currentOccupancy(-1)
                .build();

        mockMvc.perform(put("/api/v1/rooms/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

        verify(roomService, never()).updateRoom(anyLong(), any(RoomUpdateDto.class));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void findAllRooms_ShouldReturnListOfRooms() throws Exception {
        List<RoomResponseDto> rooms = List.of(roomResponseDto);
        when(roomService.getAllRooms()).thenReturn(rooms);

        mockMvc.perform(get("/api/v1/rooms/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].number").value("101"))  // Изменено с roomNumber на number
                .andExpect(jsonPath("$[0].capacity").value(2));

        verify(roomService, times(1)).getAllRooms();
    }

    @Test
    @WithMockUser(authorities = {"MANAGER"})
    void findAllRooms_WithManagerAuthority_ShouldReturnOk() throws Exception {
        when(roomService.getAllRooms()).thenReturn(List.of(roomResponseDto));

        mockMvc.perform(get("/api/v1/rooms/get-all"))
                .andExpect(status().isOk());

        verify(roomService, times(1)).getAllRooms();
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void deleteRoom_ShouldReturnNoContent() throws Exception {
        doNothing().when(roomService).deleteRoom(1L);

        mockMvc.perform(delete("/api/v1/rooms/1"))
                .andExpect(status().isNoContent());

        verify(roomService, times(1)).deleteRoom(1L);
    }

    @Test
    @WithMockUser(authorities = {"MANAGER"})
    void deleteRoom_WithManagerAuthority_ShouldReturnNoContent() throws Exception {
        doNothing().when(roomService).deleteRoom(1L);

        mockMvc.perform(delete("/api/v1/rooms/1"))
                .andExpect(status().isNoContent());

        verify(roomService, times(1)).deleteRoom(1L);
    }

    // Отладочный тест для проверки JSON структуры
    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void createRoom_DebugJsonResponse() throws Exception {
        when(roomService.createRoom(any(RoomRequestDto.class))).thenReturn(roomResponseDto);

        String response = mockMvc.perform(post("/api/v1/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roomRequestDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        System.out.println("Response JSON: " + response);
    }
}