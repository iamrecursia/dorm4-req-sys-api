package com.kozitskiy.dorm4.sys.controllers;

import com.kozitskiy.dorm4.sys.dto.equipment.EquipmentCreateDto;
import com.kozitskiy.dorm4.sys.dto.equipment.EquipmentResponseDto;
import com.kozitskiy.dorm4.sys.dto.equipment.EquipmentUpdateDto;
import com.kozitskiy.dorm4.sys.mapper.EquipmentMapper;
import com.kozitskiy.dorm4.sys.service.equipment.EquipmentService;
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
@RequestMapping("api/v1/equipments")
@RequiredArgsConstructor
@Tag(name = "Equipment Management", description = "API for equipment management")
public class EquipmentController {
    private final EquipmentService equipmentService;
    private final EquipmentMapper equipmentMapper;

    @PostMapping
    @Operation(summary = "Create equipment")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public ResponseEntity<EquipmentResponseDto> createEquipment(@RequestBody @Valid EquipmentCreateDto equipment) {
        EquipmentResponseDto response = equipmentService.createEquipment(equipment);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update equipment")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public ResponseEntity<EquipmentResponseDto> updateEquipment(@PathVariable Long id, @RequestBody @Valid EquipmentUpdateDto equipment) {
        EquipmentResponseDto updatedEquipment = equipmentService.updateEquipment(id, equipment);
        return new ResponseEntity<>(updatedEquipment, HttpStatus.OK);
    }

    @GetMapping("/get-all")
    @Operation(summary = "Get all equipments")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public ResponseEntity<List<EquipmentResponseDto>> getAllEquipment() {
        return ResponseEntity.ok(equipmentService.getAllEquipments());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete equipment by id")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
        return ResponseEntity.ok().build();
    }
}
