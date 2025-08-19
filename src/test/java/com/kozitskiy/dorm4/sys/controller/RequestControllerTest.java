package com.kozitskiy.dorm4.sys.controller;

import com.kozitskiy.dorm4.sys.controllers.RequestController;
import com.kozitskiy.dorm4.sys.dto.request.RequestCreateDto;
import com.kozitskiy.dorm4.sys.dto.request.RequestResponseDto;
import com.kozitskiy.dorm4.sys.entities.enums.RequestStatus;
import com.kozitskiy.dorm4.sys.entities.enums.RequestType;
import com.kozitskiy.dorm4.sys.service.request.RequestService;
import com.kozitskiy.dorm4.sys.service.user.UserService;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestControllerTest {

    @Mock
    private RequestService requestService;

    @Mock
    private UserService userService;

    @InjectMocks
    private RequestController requestController;

    @Test
    void createRequest_ShouldReturnCreatedRequestWithStatus201_WhenValidDtoProvided() {
        // Arrange
        RequestCreateDto requestCreateDto = new RequestCreateDto(
                "Test Request",
                "Test description",
                RequestType.PLUMBER
        );

        RequestResponseDto expectedResponse = RequestResponseDto.builder()
                .id(1L)
                .title("Test Request")
                .description("Test description")
                .status(RequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        when(requestService.createRequest(any(RequestCreateDto.class))).thenReturn(expectedResponse);

        // Mock authentication for STUDENT
        Authentication auth = new TestingAuthenticationToken(
                "student",
                null,
                List.of(new SimpleGrantedAuthority("STUDENT"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act
        ResponseEntity<RequestResponseDto> response = requestController.createRequest(requestCreateDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        RequestResponseDto actualResponse = response.getBody();
        assertEquals(1L, actualResponse.id());
        assertEquals("Test Request", actualResponse.title());
        assertEquals("Test description", actualResponse.description());
        assertEquals(RequestStatus.PENDING, actualResponse.status());
        assertNotNull(actualResponse.createdAt());

        verify(requestService, times(1)).createRequest(requestCreateDto);
    }

    @Test
    void createRequest_ShouldWorkForAllAuthorizedRoles() {
        // Test for STUDENT
        testCreateRequestForRole("STUDENT");

        // Test for ADMIN
        testCreateRequestForRole("ADMIN");

        // Test for MANAGER
        testCreateRequestForRole("MANAGER");
    }

    private void testCreateRequestForRole(String role) {
        // Arrange
        RequestCreateDto requestCreateDto = new RequestCreateDto(
                role + " Request",
                "Description for " + role,
                RequestType.PLUMBER
        );

        RequestResponseDto expectedResponse = RequestResponseDto.builder()
                .id(2L)
                .title(role + " Request")
                .description("Description for " + role)
                .status(RequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        when(requestService.createRequest(any(RequestCreateDto.class))).thenReturn(expectedResponse);

        // Mock authentication for the role
        Authentication auth = new TestingAuthenticationToken(
                role.toLowerCase(),
                null,
                List.of(new SimpleGrantedAuthority(role))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act
        ResponseEntity<RequestResponseDto> response = requestController.createRequest(requestCreateDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(role + " Request", response.getBody().title());

        verify(requestService, times(1)).createRequest(requestCreateDto);

        // Reset mock for next test
        reset(requestService);
    }

    @Test
    void createRequest_ShouldCallServiceWithCorrectParameters() {
        // Arrange
        RequestCreateDto requestCreateDto = new RequestCreateDto(
                "Specific Title",
                "Specific Description",
                RequestType.PLUMBER
        );

        RequestResponseDto expectedResponse = RequestResponseDto.builder()
                .id(3L)
                .title("Specific Title")
                .description("Specific Description")
                .status(RequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        when(requestService.createRequest(any(RequestCreateDto.class))).thenReturn(expectedResponse);

        // Mock authentication
        Authentication auth = new TestingAuthenticationToken(
                "user",
                null,
                List.of(new SimpleGrantedAuthority("STUDENT"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Act
        ResponseEntity<RequestResponseDto> response = requestController.createRequest(requestCreateDto);

        // Assert
        verify(requestService, times(1)).createRequest(requestCreateDto);
        assertNotNull(response.getBody());
        assertEquals("Specific Title", response.getBody().title());
        assertEquals(RequestType.PLUMBER, requestCreateDto.requestType());
    }
}