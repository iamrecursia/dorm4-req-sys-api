package com.kozitskiy.dorm4.sys.service;

import com.kozitskiy.dorm4.sys.dto.equipment.EquipmentCreateDto;
import com.kozitskiy.dorm4.sys.dto.equipment.EquipmentResponseDto;
import com.kozitskiy.dorm4.sys.dto.equipment.EquipmentUpdateDto;
import com.kozitskiy.dorm4.sys.entities.Equipment;
import com.kozitskiy.dorm4.sys.exceptions.EquipmentNotFoundException;
import com.kozitskiy.dorm4.sys.mapper.EquipmentMapper;
import com.kozitskiy.dorm4.sys.repositories.EquipmentRepository;
import com.kozitskiy.dorm4.sys.repositories.RoomRepository;
import com.kozitskiy.dorm4.sys.service.equipment.EquipmentServiceImpl;
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
public class EquipmentServiceImplTest {
    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private EquipmentMapper equipmentMapper;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private EquipmentServiceImpl equipmentService;

    @Test
    void createEquipment_ValidData_ShouldCreateAndReturnEquipment() {
        // Arrange
        EquipmentCreateDto dto = new EquipmentCreateDto("Laptop", "Gaming laptop", "WORKING");

        Equipment equipmentToSave = Equipment.builder()
                .name("Laptop")
                .description("Gaming laptop")
                .status("WORKING")
                .build();

        Equipment savedEquipment = Equipment.builder()
                .id(1L)
                .name("Laptop")
                .description("Gaming laptop")
                .status("WORKING")
                .build();

        when(equipmentRepository.save(any(Equipment.class))).thenReturn(savedEquipment);

        // Act
        EquipmentResponseDto result = equipmentService.createEquipment(dto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Laptop", result.name());
        assertEquals("Gaming laptop", result.description());
        assertEquals("WORKING", result.status());

        verify(equipmentRepository).save(any(Equipment.class));
    }

    @Test
    void createEquipment_NullName_ShouldCreateEquipment() {
        // Arrange
        EquipmentCreateDto dto = new EquipmentCreateDto(null, "Test description", "WORKING");

        Equipment equipment = Equipment.builder()
                .id(1L)
                .name(null)
                .description("Test description")
                .status("WORKING")
                .build();

        EquipmentResponseDto expectedResponse = EquipmentResponseDto.builder()
                .id(1L)
                .name(null)
                .description("Test description")
                .status("WORKING")
                .build();

        when(equipmentRepository.save(any(Equipment.class))).thenReturn(equipment);

        // Act
        EquipmentResponseDto result = equipmentService.createEquipment(dto);

        // Assert
        assertNotNull(result);
        assertNull(result.name());
        assertEquals("Test description", result.description());

        verify(equipmentRepository).save(any(Equipment.class));
    }

    @Test
    void updateEquipment_ValidData_ShouldUpdateAndReturnEquipment() {
        // Arrange
        Long equipmentId = 1L;
        EquipmentUpdateDto updateDto = new EquipmentUpdateDto("Updated Laptop", "Updated description", "WORKING");

        Equipment existingEquipment = Equipment.builder()
                .id(equipmentId)
                .name("Old Laptop")
                .description("Old description")
                .status("WORKING")
                .build();

        Equipment updatedEquipment = Equipment.builder()
                .id(equipmentId)
                .name("Updated Laptop")
                .description("Updated description")
                .status("WORKING")
                .build();

        when(equipmentRepository.findById(equipmentId)).thenReturn(Optional.of(existingEquipment));
        when(equipmentRepository.save(any(Equipment.class))).thenReturn(updatedEquipment);

        // Act
        EquipmentResponseDto result = equipmentService.updateEquipment(equipmentId, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals(equipmentId, result.id());
        assertEquals("Updated Laptop", result.name());
        assertEquals("Updated description", result.description());
        assertEquals("WORKING", result.status());

        verify(equipmentRepository).findById(equipmentId);
        verify(equipmentRepository).save(existingEquipment);
    }

    @Test
    void updateEquipment_EquipmentNotFound_ShouldThrowEquipmentNotFoundException() {
        // Arrange
        Long equipmentId = 1L;
        EquipmentUpdateDto updateDto = new EquipmentUpdateDto("Updated", "Desc", "WORKING");

        when(equipmentRepository.findById(equipmentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EquipmentNotFoundException.class,
                () -> equipmentService.updateEquipment(equipmentId, updateDto));

        verify(equipmentRepository).findById(equipmentId);
        verify(equipmentRepository, never()).save(any(Equipment.class));
    }

    @Test
    void updateEquipment_NullFields_ShouldUpdateWithNullValues() {
        // Arrange
        Long equipmentId = 1L;
        EquipmentUpdateDto updateDto = new EquipmentUpdateDto(null, null, null);

        Equipment existingEquipment = Equipment.builder()
                .id(equipmentId)
                .name("Old Name")
                .description("Old Description")
                .status("WORKING")
                .build();

        Equipment updatedEquipment = Equipment.builder()
                .id(equipmentId)
                .name(null)
                .description(null)
                .status(null)
                .build();

        when(equipmentRepository.findById(equipmentId)).thenReturn(Optional.of(existingEquipment));
        when(equipmentRepository.save(any(Equipment.class))).thenReturn(updatedEquipment);

        // Act
        EquipmentResponseDto result = equipmentService.updateEquipment(equipmentId, updateDto);

        // Assert
        assertNotNull(result);
        assertNull(result.name());
        assertNull(result.description());
        assertNull(result.status());

        verify(equipmentRepository).findById(equipmentId);
        verify(equipmentRepository).save(existingEquipment);
    }

    @Test
    void getAllEquipments_EquipmentsExist_ShouldReturnAllEquipments() {
        // Arrange
        Equipment equipment1 = Equipment.builder()
                .id(1L)
                .name("Laptop")
                .description("Gaming laptop")
                .status("WORKING")
                .build();

        Equipment equipment2 = Equipment.builder()
                .id(2L)
                .name("Projector")
                .description("HD projector")
                .status("WORKING")
                .build();

        EquipmentResponseDto dto1 = new EquipmentResponseDto(1L, "Laptop", "Gaming laptop", "WORKING");
        EquipmentResponseDto dto2 = new EquipmentResponseDto(2L, "Projector", "HD projector", "WORKING");

        when(equipmentRepository.findAll()).thenReturn(List.of(equipment1, equipment2));
        when(equipmentMapper.convertToDto(equipment1)).thenReturn(dto1);
        when(equipmentMapper.convertToDto(equipment2)).thenReturn(dto2);

        // Act
        List<EquipmentResponseDto> result = equipmentService.getAllEquipments();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(1L, result.get(0).id());
        assertEquals("Laptop", result.get(0).name());
        assertEquals("Gaming laptop", result.get(0).description());

        assertEquals(2L, result.get(1).id());
        assertEquals("Projector", result.get(1).name());
        assertEquals("HD projector", result.get(1).description());

        verify(equipmentRepository).findAll();
        verify(equipmentMapper, times(2)).convertToDto(any(Equipment.class));
    }

    @Test
    void getAllEquipments_NoEquipments_ShouldReturnEmptyList() {
        // Arrange
        when(equipmentRepository.findAll()).thenReturn(List.of());

        // Act
        List<EquipmentResponseDto> result = equipmentService.getAllEquipments();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(equipmentRepository).findAll();
        verify(equipmentMapper, never()).convertToDto(any(Equipment.class));
    }

    @Test
    void deleteEquipment_ValidId_ShouldDeleteEquipment() {
        // Arrange
        Long equipmentId = 1L;
        doNothing().when(equipmentRepository).deleteById(equipmentId);

        // Act
        equipmentService.deleteEquipment(equipmentId);

        // Assert
        verify(equipmentRepository).deleteById(equipmentId);
    }

    @Test
    void deleteEquipment_NullId_ShouldCallRepositoryWithNull() {
        // Arrange
        Long equipmentId = null;
        doNothing().when(equipmentRepository).deleteById(equipmentId);

        // Act
        equipmentService.deleteEquipment(equipmentId);

        // Assert
        verify(equipmentRepository).deleteById(null);
    }

    @Test
    void createEquipment_EmptyStrings_ShouldCreateEquipmentWithEmptyValues() {
        // Arrange
        EquipmentCreateDto dto = new EquipmentCreateDto("", "", "WORKING");

        Equipment equipment = Equipment.builder()
                .id(1L)
                .name("")
                .description("")
                .status("WORKING")
                .build();

        EquipmentResponseDto expectedResponse = EquipmentResponseDto.builder()
                .id(1L)
                .name("")
                .description("")
                .status("WORKING")
                .build();

        when(equipmentRepository.save(any(Equipment.class))).thenReturn(equipment);

        // Act
        EquipmentResponseDto result = equipmentService.createEquipment(dto);

        // Assert
        assertNotNull(result);
        assertEquals("", result.name());
        assertEquals("", result.description());

        verify(equipmentRepository).save(any(Equipment.class));
    }

    @Test
    void updateEquipment_PartialUpdate_ShouldUpdateOnlyProvidedFields() {
        // Arrange
        Long equipmentId = 1L;
        EquipmentUpdateDto updateDto = new EquipmentUpdateDto("New Name", null, null);

        Equipment existingEquipment = Equipment.builder()
                .id(equipmentId)
                .name("Old Name")
                .description("Old Description")
                .status("WORKING")
                .build();

        Equipment updatedEquipment = Equipment.builder()
                .id(equipmentId)
                .name("New Name")
                .description("Old Description")
                .status("WORKING")
                .build();

        when(equipmentRepository.findById(equipmentId)).thenReturn(Optional.of(existingEquipment));
        when(equipmentRepository.save(any(Equipment.class))).thenReturn(updatedEquipment);

        // Act
        EquipmentResponseDto result = equipmentService.updateEquipment(equipmentId, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals("New Name", result.name());
        assertEquals("Old Description", result.description());
        assertEquals("WORKING", result.status());

        verify(equipmentRepository).findById(equipmentId);
        verify(equipmentRepository).save(existingEquipment);
    }

    @Test
    void getAllEquipments_MapperReturnsNull_ShouldHandleNullGracefully() {
        // Arrange
        Equipment equipment = Equipment.builder()
                .id(1L)
                .name("Test")
                .description("Test")
                .status("WORKING")
                .build();

        when(equipmentRepository.findAll()).thenReturn(List.of(equipment));
        when(equipmentMapper.convertToDto(equipment)).thenReturn(null);

        // Act
        List<EquipmentResponseDto> result = equipmentService.getAllEquipments();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertNull(result.get(0));

        verify(equipmentRepository).findAll();
        verify(equipmentMapper).convertToDto(equipment);
    }


}
