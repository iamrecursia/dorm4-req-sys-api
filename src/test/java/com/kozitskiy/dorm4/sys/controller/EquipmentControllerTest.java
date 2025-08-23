package com.kozitskiy.dorm4.sys.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kozitskiy.dorm4.sys.controllers.EquipmentController;
import com.kozitskiy.dorm4.sys.dto.equipment.EquipmentCreateDto;
import com.kozitskiy.dorm4.sys.dto.equipment.EquipmentResponseDto;
import com.kozitskiy.dorm4.sys.dto.equipment.EquipmentUpdateDto;
import com.kozitskiy.dorm4.sys.mapper.EquipmentMapper;
import com.kozitskiy.dorm4.sys.service.equipment.EquipmentService;
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
class EquipmentControllerTest {

    @Mock
    private EquipmentService equipmentService;

    @Mock
    private EquipmentMapper equipmentMapper;

    @InjectMocks
    private EquipmentController equipmentController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private EquipmentCreateDto equipmentCreateDto;
    private EquipmentResponseDto equipmentResponseDto;
    private EquipmentUpdateDto equipmentUpdateDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(equipmentController).build();
        objectMapper = new ObjectMapper();

        // Initialize test data
        equipmentCreateDto = EquipmentCreateDto.builder()
                .name("Computer")
                .description("Gaming PC")
                .status("WORKING")
                .build();

        equipmentResponseDto = EquipmentResponseDto.builder()
                .id(1L)
                .name("Computer")
                .description("Gaming PC")
                .status("WORKING")
                .build();

        equipmentUpdateDto = EquipmentUpdateDto.builder()
                .name("Updated Computer")
                .description("Updated Gaming PC")
                .status("REPAIR")
                .build();
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void createEquipment_WithValidData_ShouldReturnCreated() throws Exception {
        when(equipmentService.createEquipment(any(EquipmentCreateDto.class))).thenReturn(equipmentResponseDto);

        mockMvc.perform(post("/api/v1/equipment/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(equipmentCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Computer"))
                .andExpect(jsonPath("$.description").value("Gaming PC"))
                .andExpect(jsonPath("$.status").value("WORKING"));

        verify(equipmentService, times(1)).createEquipment(any(EquipmentCreateDto.class));
    }

    @Test
    @WithMockUser(authorities = {"MANAGER"})
    void createEquipment_WithManagerAuthority_ShouldReturnCreated() throws Exception {
        when(equipmentService.createEquipment(any(EquipmentCreateDto.class))).thenReturn(equipmentResponseDto);

        mockMvc.perform(post("/api/v1/equipment/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(equipmentCreateDto)))
                .andExpect(status().isCreated());

        verify(equipmentService, times(1)).createEquipment(any(EquipmentCreateDto.class));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void createEquipment_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        EquipmentCreateDto invalidDto = EquipmentCreateDto.builder()
                .name("")  // Blank name - invalid
                .description("")  // Blank description - invalid
                .status("INVALID_STATUS")  // Invalid status
                .build();

        mockMvc.perform(post("/api/v1/equipment/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

        verify(equipmentService, never()).createEquipment(any(EquipmentCreateDto.class));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void createEquipment_WithMissingRequiredFields_ShouldReturnBadRequest() throws Exception {
        // Create DTO without required fields
        String invalidJson = "{}";

        mockMvc.perform(post("/api/v1/equipment/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());

        verify(equipmentService, never()).createEquipment(any(EquipmentCreateDto.class));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void updateEquipment_WithValidData_ShouldReturnOk() throws Exception {
        when(equipmentService.updateEquipment(eq(1L), any(EquipmentUpdateDto.class))).thenReturn(equipmentResponseDto);

        mockMvc.perform(put("/api/v1/equipment/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(equipmentUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Computer"))
                .andExpect(jsonPath("$.status").value("WORKING"));

        verify(equipmentService, times(1)).updateEquipment(eq(1L), any(EquipmentUpdateDto.class));
    }

    @Test
    @WithMockUser(authorities = {"MANAGER"})
    void updateEquipment_WithManagerAuthority_ShouldReturnOk() throws Exception {
        when(equipmentService.updateEquipment(eq(1L), any(EquipmentUpdateDto.class))).thenReturn(equipmentResponseDto);

        mockMvc.perform(put("/api/v1/equipment/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(equipmentUpdateDto)))
                .andExpect(status().isOk());

        verify(equipmentService, times(1)).updateEquipment(eq(1L), any(EquipmentUpdateDto.class));
    }

//    @Test
//    @WithMockUser(authorities = {"USER"})
//    void updateEquipment_WithUserAuthority_ShouldReturnForbidden() throws Exception {
//        mockMvc.perform(put("/api/v1/equipment/update/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(equipmentUpdateDto)))
//                .andExpect(status().isForbidden());
//
//        verify(equipmentService, never()).updateEquipment(anyLong(), any(EquipmentUpdateDto.class));
//    }
//
//    @Test
//    @WithMockUser(authorities = {"ADMIN"})
//    void updateEquipment_WithInvalidStatus_ShouldReturnBadRequest() throws Exception {
//        EquipmentUpdateDto invalidDto = EquipmentUpdateDto.builder()
//                .status("INVALID_STATUS")  // Invalid status pattern
//                .build();
//
//        mockMvc.perform(put("/api/v1/equipment/update/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalidDto)))
//                .andExpect(status().isBadRequest());
//
//        verify(equipmentService, never()).updateEquipment(anyLong(), any(EquipmentUpdateDto.class));
//    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void getAllEquipment_ShouldReturnListOfEquipment() throws Exception {
        List<EquipmentResponseDto> equipmentList = List.of(equipmentResponseDto);
        when(equipmentService.getAllEquipments()).thenReturn(equipmentList);

        mockMvc.perform(get("/api/v1/equipment/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Computer"))
                .andExpect(jsonPath("$[0].description").value("Gaming PC"));

        verify(equipmentService, times(1)).getAllEquipments();
    }

    @Test
    @WithMockUser(authorities = {"MANAGER"})
    void getAllEquipment_WithManagerAuthority_ShouldReturnOk() throws Exception {
        when(equipmentService.getAllEquipments()).thenReturn(List.of(equipmentResponseDto));

        mockMvc.perform(get("/api/v1/equipment/get-all"))
                .andExpect(status().isOk());

        verify(equipmentService, times(1)).getAllEquipments();
    }

//    @Test
//    @WithMockUser(authorities = {"USER"})
//    void getAllEquipment_WithUserAuthority_ShouldReturnForbidden() throws Exception {
//        mockMvc.perform(get("/api/v1/equipment/get-all"))
//                .andExpect(status().isForbidden());
//
//        verify(equipmentService, never()).getAllEquipments();
//    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void deleteEquipment_ShouldReturnOk() throws Exception {
        doNothing().when(equipmentService).deleteEquipment(1L);

        mockMvc.perform(delete("/api/v1/equipment/delete/1"))
                .andExpect(status().isOk());

        verify(equipmentService, times(1)).deleteEquipment(1L);
    }

    @Test
    @WithMockUser(authorities = {"MANAGER"})
    void deleteEquipment_WithManagerAuthority_ShouldReturnOk() throws Exception {
        doNothing().when(equipmentService).deleteEquipment(1L);

        mockMvc.perform(delete("/api/v1/equipment/delete/1"))
                .andExpect(status().isOk());

        verify(equipmentService, times(1)).deleteEquipment(1L);
    }
//
//    @Test
//    @WithMockUser(authorities = {"USER"})
//    void deleteEquipment_WithUserAuthority_ShouldReturnForbidden() throws Exception {
//        mockMvc.perform(delete("/api/v1/equipment/delete/1"))
//                .andExpect(status().isForbidden());
//
//        verify(equipmentService, never()).deleteEquipment(anyLong());
//    }
//
//    @Test
//    void createEquipment_WithoutAuthentication_ShouldReturnUnauthorized() throws Exception {
//        mockMvc.perform(post("/api/v1/equipment/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(equipmentCreateDto)))
//                .andExpect(status().isUnauthorized());
//
//        verify(equipmentService, never()).createEquipment(any(EquipmentCreateDto.class));
//    }
//
//    @Test
//    @WithMockUser(authorities = {"ADMIN"})
//    void updateEquipment_WithNonExistingId_ShouldHandleServiceException() throws Exception {
//        when(equipmentService.updateEquipment(eq(999L), any(EquipmentUpdateDto.class)))
//                .thenThrow(new RuntimeException("Equipment not found"));
//
//        mockMvc.perform(put("/api/v1/equipment/update/999")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(equipmentUpdateDto)))
//                .andExpect(status().isInternalServerError());
//
//        verify(equipmentService, times(1)).updateEquipment(eq(999L), any(EquipmentUpdateDto.class));
//    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void getAllEquipment_WhenNoEquipment_ShouldReturnEmptyList() throws Exception {
        when(equipmentService.getAllEquipments()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/equipment/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(equipmentService, times(1)).getAllEquipments();
    }

    // Test for partial update - only some fields provided
    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void updateEquipment_WithPartialData_ShouldReturnOk() throws Exception {
        EquipmentUpdateDto partialUpdateDto = EquipmentUpdateDto.builder()
                .name("Partial Update")
                .build();

        when(equipmentService.updateEquipment(eq(1L), any(EquipmentUpdateDto.class))).thenReturn(equipmentResponseDto);

        mockMvc.perform(put("/api/v1/equipment/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partialUpdateDto)))
                .andExpect(status().isOk());

        verify(equipmentService, times(1)).updateEquipment(eq(1L), any(EquipmentUpdateDto.class));
    }

    // Debug test to check JSON response structure
    @Test
    @WithMockUser(authorities = {"ADMIN"})
    void createEquipment_DebugJsonResponse() throws Exception {
        when(equipmentService.createEquipment(any(EquipmentCreateDto.class))).thenReturn(equipmentResponseDto);

        String response = mockMvc.perform(post("/api/v1/equipment/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(equipmentCreateDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        System.out.println("Response JSON: " + response);
    }
}
