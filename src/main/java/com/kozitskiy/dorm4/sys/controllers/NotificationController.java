package com.kozitskiy.dorm4.sys.controllers;

import com.kozitskiy.dorm4.sys.dto.notification.NotificationCreateDto;
import com.kozitskiy.dorm4.sys.dto.notification.NotificationResponseDto;
import com.kozitskiy.dorm4.sys.entities.Notification;
import com.kozitskiy.dorm4.sys.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping
    @Operation(summary = "Create notification")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('PLUMBER') or hasAuthority('ELECTRICIAN')")
    public ResponseEntity<NotificationResponseDto> createNotification(@RequestBody @Valid NotificationCreateDto createDto) {
        NotificationResponseDto responseDto = notificationService.createNotificationForUser(createDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponseDto> getNotificationById(@PathVariable Long id){
        NotificationResponseDto dto = notificationService.findNotificationById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

}
