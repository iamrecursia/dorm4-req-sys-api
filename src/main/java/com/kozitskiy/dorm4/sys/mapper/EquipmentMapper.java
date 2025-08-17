package com.kozitskiy.dorm4.sys.mapper;

import com.kozitskiy.dorm4.sys.dto.equipment.EquipmentResponseDto;
import com.kozitskiy.dorm4.sys.entities.Equipment;
import org.springframework.stereotype.Component;

@Component
public class EquipmentMapper {
    public EquipmentResponseDto convertToDto(Equipment equipment) {
        return new EquipmentResponseDto(
                equipment.getId(),
                equipment.getName(),
                equipment.getDescription(),
                equipment.getStatus(),
                equipment.getRoom().getId()
        );
    }

}
