package com.kozitskiy.dorm4.sys.controller;

import com.kozitskiy.dorm4.sys.controllers.DormitoryController;
import com.kozitskiy.dorm4.sys.dto.dorm.DormitoryCreateDto;
import com.kozitskiy.dorm4.sys.dto.dorm.DormitoryResponseDto;
import com.kozitskiy.dorm4.sys.service.dorm.DormitoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DormitoryControllerTest {

    @Mock
    private DormitoryService dormitoryService;

    @InjectMocks
    private DormitoryController dormitoryController;

    @Test
    void createDormitory_ShouldReturnCreatedDormitoryWithStatus201_WhenValidDtoProvided() {
        // Arrange
        DormitoryCreateDto createDto = new DormitoryCreateDto(
                "Dormitory A",
                "123 Main Street",
                5
        );

        DormitoryResponseDto expectedResponse = DormitoryResponseDto.builder()
                .id(1L)
                .name("Dormitory A")
                .address("123 Main Street")
                .floorsCount(5)
                .build();

        when(dormitoryService.createDorm(any(DormitoryCreateDto.class))).thenReturn(expectedResponse);

        // Mock authentication for ADMIN
        Authentication auth = new TestingAuthenticationToken(
                "admin",
                null,
                List.of(new SimpleGrantedAuthority("ADMIN"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act
        ResponseEntity<DormitoryResponseDto> response = dormitoryController.createDormitory(createDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        DormitoryResponseDto actualResponse = response.getBody();
        assertEquals(1L, actualResponse.id());
        assertEquals("Dormitory A", actualResponse.name());
        assertEquals("123 Main Street", actualResponse.address());
        assertEquals(5, actualResponse.floorsCount());

        verify(dormitoryService, times(1)).createDorm(createDto);
    }

    @Test
    void createDormitory_ShouldCallServiceWithCorrectParameters() {
        // Arrange
        DormitoryCreateDto createDto = new DormitoryCreateDto(
                "Specific Dorm",
                "456 Oak Avenue",
                7
        );

        DormitoryResponseDto expectedResponse = DormitoryResponseDto.builder()
                .id(2L)
                .name("Specific Dorm")
                .address("456 Oak Avenue")
                .floorsCount(7)
                .build();

        when(dormitoryService.createDorm(any(DormitoryCreateDto.class))).thenReturn(expectedResponse);

        // Mock authentication for ADMIN
        Authentication auth = new TestingAuthenticationToken(
                "admin",
                null,
                List.of(new SimpleGrantedAuthority("ADMIN"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act
        ResponseEntity<DormitoryResponseDto> response = dormitoryController.createDormitory(createDto);

        // Assert
        verify(dormitoryService, times(1)).createDorm(createDto);
        assertNotNull(response.getBody());
        assertEquals("Specific Dorm", response.getBody().name());
        assertEquals("456 Oak Avenue", response.getBody().address());
        assertEquals(7, response.getBody().floorsCount());
    }

    @Test
    void createDormitory_ShouldHandleNullFloorsCount() {
        // Arrange
        DormitoryCreateDto createDto = new DormitoryCreateDto(
                "Dormitory B",
                "789 Pine Road",
                null
        );

        DormitoryResponseDto expectedResponse = DormitoryResponseDto.builder()
                .id(3L)
                .name("Dormitory B")
                .address("789 Pine Road")
                .floorsCount(null)
                .build();

        when(dormitoryService.createDorm(any(DormitoryCreateDto.class))).thenReturn(expectedResponse);

        // Mock authentication for ADMIN
        Authentication auth = new TestingAuthenticationToken(
                "admin",
                null,
                List.of(new SimpleGrantedAuthority("ADMIN"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act
        ResponseEntity<DormitoryResponseDto> response = dormitoryController.createDormitory(createDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().floorsCount());
        verify(dormitoryService, times(1)).createDorm(createDto);
    }
}