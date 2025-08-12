package com.kozitskiy.dorm4.sys.controllers;

import com.kozitskiy.dorm4.sys.dto.AdminUserCreateDto;
import com.kozitskiy.dorm4.sys.dto.UserCreateDto;
import com.kozitskiy.dorm4.sys.dto.UserResponseDto;
import com.kozitskiy.dorm4.sys.entities.User;
import com.kozitskiy.dorm4.sys.entities.enums.UserType;
import com.kozitskiy.dorm4.sys.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // Доступен всем (для регистрации)
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserCreateDto userCreateDto) {
        return ResponseEntity.ok(userService.createUser(userCreateDto));
    }

    // Создания пользователя админом
    @PostMapping("/by-admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<User> createUser(@RequestBody AdminUserCreateDto adminUserCreateDto){
        return new ResponseEntity<>(userService.createUserByAdmin(adminUserCreateDto), HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long userId){
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.FOUND);
    }

    @GetMapping("/by-username/{userName}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<User> getUserByName(@PathVariable String userName){
        return new ResponseEntity<>(userService.findUserByUsername(userName), HttpStatus.FOUND);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getAllUsers(){
        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.FOUND);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long userId){
        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }

}
