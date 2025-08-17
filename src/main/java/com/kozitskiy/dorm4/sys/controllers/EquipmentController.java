package com.kozitskiy.dorm4.sys.controllers;

import com.kozitskiy.dorm4.sys.dto.equipment.EquipmentCreateDto;
import com.kozitskiy.dorm4.sys.dto.equipment.EquipmentResponseDto;
import com.kozitskiy.dorm4.sys.dto.equipment.EquipmentUpdateDto;
import com.kozitskiy.dorm4.sys.entities.Equipment;
import com.kozitskiy.dorm4.sys.mapper.EquipmentMapper;
import com.kozitskiy.dorm4.sys.service.EquipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/equipment")
@RequiredArgsConstructor
@Tag(name = "Equipment Management", description = "API for equipment management")
public class EquipmentController {
    private final EquipmentService equipmentService;
    private final EquipmentMapper equipmentMapper;

    @PostMapping("/create")
    @Operation(summary = "Create equipment")
    public ResponseEntity<EquipmentResponseDto> createEquipment(@RequestBody @Valid EquipmentCreateDto equipment) {
        EquipmentResponseDto response = equipmentService.createEquipment(equipment);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update equipment")
    public ResponseEntity<EquipmentResponseDto> updateEquipment(@PathVariable Long id, @RequestBody @Valid EquipmentUpdateDto equipment) {
        EquipmentResponseDto updatedEquipment = equipmentService.updateEquipment(id, equipment);
        return new ResponseEntity<>(updatedEquipment, HttpStatus.OK);
    }

    @GetMapping("/get-all")
    @Operation(summary = "Get all equipments")
    public ResponseEntity<List<EquipmentResponseDto>> getAllEquipment() {
        return ResponseEntity.ok(equipmentService.getAllEquipments());
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete equipment by id")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
        return ResponseEntity.ok().build();
    }

}
