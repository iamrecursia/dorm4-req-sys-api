package com.kozitskiy.dorm4.sys.mapper;

import com.kozitskiy.dorm4.sys.dto.equipment.EquipmentCreateDto;
import com.kozitskiy.dorm4.sys.dto.equipment.EquipmentResponseDto;
import com.kozitskiy.dorm4.sys.dto.equipment.EquipmentUpdateDto;
import com.kozitskiy.dorm4.sys.entities.Equipment;
import org.springframework.stereotype.Component;

@Component
public class EquipmentMapper {
    public EquipmentResponseDto convertToDto(Equipment equipment) {
        return new EquipmentResponseDto(
                equipment.getId(),
                equipment.getName(),
                equipment.getDescription(),
                equipment.getStatus()
        );
    }

    public Equipment convertToEntity(EquipmentCreateDto equipmentCreateDto) {
        return Equipment.builder()
                .name(equipmentCreateDto.name())
                .description(equipmentCreateDto.description())
                .status(equipmentCreateDto.status())
                .build();
    }

    public void updateEntityFromDto(EquipmentUpdateDto equipmentUpdateDto, Equipment equipment) {
        if(equipmentUpdateDto.name() != null) {
            equipment.setName(equipmentUpdateDto.name());
        }
        if(equipmentUpdateDto.description() != null) {
            equipment.setDescription(equipmentUpdateDto.description());
        }
        if(equipmentUpdateDto.status() != null) {
            equipment.setStatus(equipmentUpdateDto.status());
        }
    }



}
