package com.kozitskiy.dorm4.sys.service;

import com.kozitskiy.dorm4.sys.dto.dorm.DormitoryCreateDto;
import com.kozitskiy.dorm4.sys.dto.dorm.DormitoryResponseDto;
import com.kozitskiy.dorm4.sys.dto.dorm.DormitoryUpdateDto;
import com.kozitskiy.dorm4.sys.entities.Dormitory;
import com.kozitskiy.dorm4.sys.exceptions.DormNotFoundException;
import com.kozitskiy.dorm4.sys.repositories.DormitoryRepository;
import com.kozitskiy.dorm4.sys.service.dorm.DormitoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DormitoryServiceImplTest {
    @Mock
    private DormitoryRepository dormitoryRepository;

    @InjectMocks
    private DormitoryServiceImpl dormitoryService;

    @Test
    void createDorm_ValidData_ShouldCreateAndReturnDormitory() {
        // Arrange
        DormitoryCreateDto dto = new DormitoryCreateDto("Dorm A", "123 Main St", 5);

        Dormitory dormitoryToSave = Dormitory.builder()
                .name("Dorm A")
                .address("123 Main St")
                .floorsCount(5)
                .rooms(new ArrayList<>())
                .build();

        Dormitory savedDormitory = Dormitory.builder()
                .id(1L)
                .name("Dorm A")
                .address("123 Main St")
                .floorsCount(5)
                .rooms(new ArrayList<>())
                .build();

        when(dormitoryRepository.save(any(Dormitory.class))).thenReturn(savedDormitory);

        // Act
        DormitoryResponseDto result = dormitoryService.createDorm(dto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Dorm A", result.name());
        assertEquals("123 Main St", result.address());
        assertEquals(5, result.floorsCount());

        verify(dormitoryRepository).save(any(Dormitory.class));
    }

    @Test
    void createDorm_NullName_ShouldCreateDormitoryWithNullName() {
        // Arrange
        DormitoryCreateDto dto = new DormitoryCreateDto(null, "123 Main St", 5);

        Dormitory savedDormitory = Dormitory.builder()
                .id(1L)
                .name(null)
                .address("123 Main St")
                .floorsCount(5)
                .rooms(new ArrayList<>())
                .build();

        when(dormitoryRepository.save(any(Dormitory.class))).thenReturn(savedDormitory);

        // Act
        DormitoryResponseDto result = dormitoryService.createDorm(dto);

        // Assert
        assertNotNull(result);
        assertNull(result.name());
        assertEquals("123 Main St", result.address());
        assertEquals(5, result.floorsCount());

        verify(dormitoryRepository).save(any(Dormitory.class));
    }

    @Test
    void updateDorm_ValidData_ShouldUpdateAndReturnDormitory() {
        // Arrange
        Long dormId = 1L;
        DormitoryUpdateDto dto = new DormitoryUpdateDto("Updated Dorm");

        Dormitory existingDormitory = Dormitory.builder()
                .id(dormId)
                .name("Old Dorm")
                .address("Old Address")
                .floorsCount(3)
                .rooms(new ArrayList<>())
                .build();

        Dormitory updatedDormitory = Dormitory.builder()
                .id(dormId)
                .name("Updated Dorm")
                .address("Old Address")
                .floorsCount(3)
                .rooms(new ArrayList<>())
                .build();

        when(dormitoryRepository.findById(dormId)).thenReturn(Optional.of(existingDormitory));
        when(dormitoryRepository.save(any(Dormitory.class))).thenReturn(updatedDormitory);

        // Act
        DormitoryResponseDto result = dormitoryService.updateDorm(dormId, dto);

        // Assert
        assertNotNull(result);
        assertEquals(dormId, result.id());
        assertEquals("Updated Dorm", result.name());
        assertEquals("Old Address", result.address()); // не изменилось
        assertEquals(3, result.floorsCount()); // не изменилось

        verify(dormitoryRepository).findById(dormId);
        verify(dormitoryRepository).save(existingDormitory);
    }

    @Test
    void updateDorm_UpdateAllFields_ShouldUpdateAllFields() {
        // Arrange
        Long dormId = 1L;
        DormitoryUpdateDto dto = new DormitoryUpdateDto("New Name");

        Dormitory existingDormitory = Dormitory.builder()
                .id(dormId)
                .name("Old Dorm")
                .address("Old Address")
                .floorsCount(3)
                .rooms(new ArrayList<>())
                .build();

        Dormitory updatedDormitory = Dormitory.builder()
                .id(dormId)
                .name("New Name")
                .address("New Address")
                .floorsCount(10)
                .rooms(new ArrayList<>())
                .build();

        when(dormitoryRepository.findById(dormId)).thenReturn(Optional.of(existingDormitory));
        when(dormitoryRepository.save(any(Dormitory.class))).thenReturn(updatedDormitory);

        // Act
        DormitoryResponseDto result = dormitoryService.updateDorm(dormId, dto);

        // Assert
        assertNotNull(result);
        assertEquals("New Name", result.name());
        assertEquals("New Address", result.address());
        assertEquals(10, result.floorsCount());

        verify(dormitoryRepository).findById(dormId);
        verify(dormitoryRepository).save(existingDormitory);
    }

    @Test
    void updateDorm_DormitoryNotFound_ShouldThrowDormNotFoundException() {
        // Arrange
        Long dormId = 1L;
        DormitoryUpdateDto dto = new DormitoryUpdateDto("New Name");

        when(dormitoryRepository.findById(dormId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DormNotFoundException.class,
                () -> dormitoryService.updateDorm(dormId, dto));

        verify(dormitoryRepository).findById(dormId);
        verify(dormitoryRepository, never()).save(any(Dormitory.class));
    }

    @Test
    void updateDorm_BlankName_ShouldNotUpdateName() {
        // Arrange
        Long dormId = 1L;
        DormitoryUpdateDto dto = new DormitoryUpdateDto("   ");

        Dormitory existingDormitory = Dormitory.builder()
                .id(dormId)
                .name("Old Dorm")
                .address("Old Address")
                .floorsCount(3)
                .rooms(new ArrayList<>())
                .build();

        Dormitory updatedDormitory = Dormitory.builder()
                .id(dormId)
                .name("Old Dorm") // не изменилось
                .address("New Address")
                .floorsCount(3)
                .rooms(new ArrayList<>())
                .build();

        when(dormitoryRepository.findById(dormId)).thenReturn(Optional.of(existingDormitory));
        when(dormitoryRepository.save(any(Dormitory.class))).thenReturn(updatedDormitory);

        // Act
        DormitoryResponseDto result = dormitoryService.updateDorm(dormId, dto);

        // Assert
        assertNotNull(result);
        assertEquals("Old Dorm", result.name()); // не изменилось
        assertEquals("New Address", result.address());

        verify(dormitoryRepository).findById(dormId);
        verify(dormitoryRepository).save(existingDormitory);
    }

    @Test
    void updateDorm_NullDtoFields_ShouldNotUpdateAnything() {
        // Arrange
        Long dormId = 1L;
        DormitoryUpdateDto dto = new DormitoryUpdateDto(null);

        Dormitory existingDormitory = Dormitory.builder()
                .id(dormId)
                .name("Old Dorm")
                .address("Old Address")
                .floorsCount(3)
                .rooms(new ArrayList<>())
                .build();

        Dormitory updatedDormitory = Dormitory.builder()
                .id(dormId)
                .name("Old Dorm") // все осталось прежним
                .address("Old Address")
                .floorsCount(3)
                .rooms(new ArrayList<>())
                .build();

        when(dormitoryRepository.findById(dormId)).thenReturn(Optional.of(existingDormitory));
        when(dormitoryRepository.save(any(Dormitory.class))).thenReturn(updatedDormitory);

        // Act
        DormitoryResponseDto result = dormitoryService.updateDorm(dormId, dto);

        // Assert
        assertNotNull(result);
        assertEquals("Old Dorm", result.name());
        assertEquals("Old Address", result.address());
        assertEquals(3, result.floorsCount());

        verify(dormitoryRepository).findById(dormId);
        verify(dormitoryRepository).save(existingDormitory);
    }

    @Test
    void deleteDorm_ValidId_ShouldDeleteDormitory() {
        // Arrange
        Long dormId = 1L;
        doNothing().when(dormitoryRepository).deleteById(dormId);

        // Act
        dormitoryService.deleteDorm(dormId);

        // Assert
        verify(dormitoryRepository).deleteById(dormId);
    }

    @Test
    void findAllDormitory_DormitoriesExist_ShouldReturnAllDormitories() {
        // Arrange
        Dormitory dormitory1 = Dormitory.builder()
                .id(1L)
                .name("Dorm A")
                .address("Address A")
                .floorsCount(5)
                .rooms(new ArrayList<>())
                .build();

        Dormitory dormitory2 = Dormitory.builder()
                .id(2L)
                .name("Dorm B")
                .address("Address B")
                .floorsCount(7)
                .rooms(new ArrayList<>())
                .build();

        when(dormitoryRepository.findAll()).thenReturn(List.of(dormitory1, dormitory2));

        // Act
        List<DormitoryResponseDto> result = dormitoryService.findAllDormitory();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(1L, result.get(0).id());
        assertEquals("Dorm A", result.get(0).name());
        assertEquals("Address A", result.get(0).address());
        assertEquals(5, result.get(0).floorsCount());

        assertEquals(2L, result.get(1).id());
        assertEquals("Dorm B", result.get(1).name());
        assertEquals("Address B", result.get(1).address());
        assertEquals(7, result.get(1).floorsCount());

        verify(dormitoryRepository).findAll();
    }

    @Test
    void findAllDormitory_NoDormitories_ShouldReturnEmptyList() {
        // Arrange
        when(dormitoryRepository.findAll()).thenReturn(List.of());

        // Act
        List<DormitoryResponseDto> result = dormitoryService.findAllDormitory();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(dormitoryRepository).findAll();
    }

    @Test
    void createDorm_ZeroFloors_ShouldCreateDormitoryWithZeroFloors() {
        // Arrange
        DormitoryCreateDto dto = new DormitoryCreateDto("Dorm A", "123 Main St", 0);

        Dormitory savedDormitory = Dormitory.builder()
                .id(1L)
                .name("Dorm A")
                .address("123 Main St")
                .floorsCount(0)
                .rooms(new ArrayList<>())
                .build();

        when(dormitoryRepository.save(any(Dormitory.class))).thenReturn(savedDormitory);

        // Act
        DormitoryResponseDto result = dormitoryService.createDorm(dto);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.floorsCount());
        verify(dormitoryRepository).save(any(Dormitory.class));
    }

    @Test
    void updateDorm_NegativeFloors_ShouldUpdateWithNegativeFloors() {
        // Arrange
        Long dormId = 1L;
        DormitoryUpdateDto dto = new DormitoryUpdateDto(null);

        Dormitory existingDormitory = Dormitory.builder()
                .id(dormId)
                .name("Old Dorm")
                .address("Old Address")
                .floorsCount(3)
                .rooms(new ArrayList<>())
                .build();

        Dormitory updatedDormitory = Dormitory.builder()
                .id(dormId)
                .name("Old Dorm")
                .address("Old Address")
                .floorsCount(-1)
                .rooms(new ArrayList<>())
                .build();

        when(dormitoryRepository.findById(dormId)).thenReturn(Optional.of(existingDormitory));
        when(dormitoryRepository.save(any(Dormitory.class))).thenReturn(updatedDormitory);

        // Act
        DormitoryResponseDto result = dormitoryService.updateDorm(dormId, dto);

        // Assert
        assertNotNull(result);
        assertEquals(-1, result.floorsCount());
        verify(dormitoryRepository).findById(dormId);
        verify(dormitoryRepository).save(existingDormitory);
    }

    @Test
    void createDorm_EmptyStrings_ShouldCreateDormitoryWithEmptyValues() {
        // Arrange
        DormitoryCreateDto dto = new DormitoryCreateDto("", "", 1);

        Dormitory savedDormitory = Dormitory.builder()
                .id(1L)
                .name("")
                .address("")
                .floorsCount(1)
                .rooms(new ArrayList<>())
                .build();

        when(dormitoryRepository.save(any(Dormitory.class))).thenReturn(savedDormitory);

        // Act
        DormitoryResponseDto result = dormitoryService.createDorm(dto);

        // Assert
        assertNotNull(result);
        assertEquals("", result.name());
        assertEquals("", result.address());
        verify(dormitoryRepository).save(any(Dormitory.class));
    }

    @Test
    void updateDorm_EmptyName_ShouldNotUpdateName() {
        // Arrange
        Long dormId = 1L;
        DormitoryUpdateDto dto = new DormitoryUpdateDto("");

        Dormitory existingDormitory = Dormitory.builder()
                .id(dormId)
                .name("Old Dorm")
                .address("Old Address")
                .floorsCount(3)
                .rooms(new ArrayList<>())
                .build();

        Dormitory updatedDormitory = Dormitory.builder()
                .id(dormId)
                .name("Old Dorm") // не изменилось
                .address("New Address")
                .floorsCount(3)
                .rooms(new ArrayList<>())
                .build();

        when(dormitoryRepository.findById(dormId)).thenReturn(Optional.of(existingDormitory));
        when(dormitoryRepository.save(any(Dormitory.class))).thenReturn(updatedDormitory);

        // Act
        DormitoryResponseDto result = dormitoryService.updateDorm(dormId, dto);

        // Assert
        assertNotNull(result);
        assertEquals("Old Dorm", result.name()); // не изменилось
        assertEquals("New Address", result.address());
        verify(dormitoryRepository).findById(dormId);
        verify(dormitoryRepository).save(existingDormitory);
    }
}
