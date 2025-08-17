package com.kozitskiy.dorm4.sys.controllers;

import com.kozitskiy.dorm4.sys.dto.dorm.DormitoryCreateDto;
import com.kozitskiy.dorm4.sys.dto.dorm.DormitoryResponseDto;
import com.kozitskiy.dorm4.sys.dto.dorm.DormitoryUpdateDto;
import com.kozitskiy.dorm4.sys.entities.Dormitory;
import com.kozitskiy.dorm4.sys.service.dorm.DormitoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/dorm")
@RequiredArgsConstructor
@Tag(name = "Dormitory Management", description = "API for dormitory management")
public class DormitoryController {
    private final DormitoryService dormitoryService;

    @PostMapping("/create")
    @Operation(summary = "Create dormitory")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<DormitoryResponseDto> createDormitory(@RequestBody @Valid DormitoryCreateDto dto) {
        DormitoryResponseDto responseDto = dormitoryService.createDorm(dto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update dormitory information")
    @PutMapping("/update/{id}")
    public ResponseEntity<DormitoryResponseDto> updateDormitory(@PathVariable Long id,
                                                     @RequestBody @Valid DormitoryUpdateDto dto
                                                     ) {
        DormitoryResponseDto response = dormitoryService.updateDorm(id, dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get all dormitories")
    @GetMapping("/get-all")
    public ResponseEntity<List<DormitoryResponseDto>> findAllDormitory() {
        return new ResponseEntity<>(dormitoryService.findAllDormitory(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete dormitory by id")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Dormitory> deleteDormitory(@PathVariable Long id) {
        dormitoryService.deleteDorm(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
