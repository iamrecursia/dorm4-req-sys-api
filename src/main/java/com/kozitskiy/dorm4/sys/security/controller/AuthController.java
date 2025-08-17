package com.kozitskiy.dorm4.sys.security.controller;

import com.kozitskiy.dorm4.sys.dto.auth.AuthRequestDto;
import com.kozitskiy.dorm4.sys.dto.auth.AuthResponseDto;
import com.kozitskiy.dorm4.sys.security.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication API")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid AuthRequestDto authRequest) {
        return ResponseEntity.ok(authService.authenticate(authRequest));
    }
}
