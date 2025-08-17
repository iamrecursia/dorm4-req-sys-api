package com.kozitskiy.dorm4.sys.service.equipment;

import com.kozitskiy.dorm4.sys.dto.equipment.EquipmentCreateDto;
import com.kozitskiy.dorm4.sys.dto.equipment.EquipmentResponseDto;
import com.kozitskiy.dorm4.sys.dto.equipment.EquipmentUpdateDto;

import java.util.List;

public interface EquipmentService {
    EquipmentResponseDto createEquipment(EquipmentCreateDto dto);
    EquipmentResponseDto updateEquipment(Long id, EquipmentUpdateDto equipmentUpdateDto);
    List<EquipmentResponseDto> getAllEquipments();
    void deleteEquipment(Long id);

}
