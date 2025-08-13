package com.kozitskiy.dorm4.sys.controllers;

import com.kozitskiy.dorm4.sys.dto.EquipmentCreateDto;
import com.kozitskiy.dorm4.sys.dto.EquipmentResponseDto;
import com.kozitskiy.dorm4.sys.dto.EquipmentUpdateDto;
import com.kozitskiy.dorm4.sys.entities.Equipment;
import com.kozitskiy.dorm4.sys.mapper.EquipmentMapper;
import com.kozitskiy.dorm4.sys.service.EquipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/equipment")
@RequiredArgsConstructor
public class EquipmentController {
    private final EquipmentService equipmentService;
    private final EquipmentMapper equipmentMapper;

    @PostMapping("/create")
    public ResponseEntity<Equipment> createEquipment(@RequestBody @Valid EquipmentCreateDto equipment) {
        Equipment createdEquipment = equipmentService.createEquipment(equipment);
        return new ResponseEntity<>(createdEquipment, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EquipmentResponseDto> updateEquipment(@PathVariable Long id, @RequestBody @Valid EquipmentUpdateDto equipment) {
        EquipmentResponseDto updatedEquipment = equipmentService.updateEquipment(id, equipment);
        return new ResponseEntity<>(updatedEquipment, HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<EquipmentResponseDto>> getAllEquipment() {
        return ResponseEntity.ok(equipmentService.getAllEquipments());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
        return ResponseEntity.ok().build();
    }

}
