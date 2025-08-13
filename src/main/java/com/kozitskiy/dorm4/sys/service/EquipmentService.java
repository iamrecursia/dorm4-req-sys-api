package com.kozitskiy.dorm4.sys.service;

import com.kozitskiy.dorm4.sys.dto.*;
import com.kozitskiy.dorm4.sys.entities.Equipment;

import java.util.List;

public interface EquipmentService {
    Equipment createEquipment(EquipmentCreateDto dto);
    EquipmentResponseDto updateEquipment(Long id, EquipmentUpdateDto equipmentUpdateDto);
    List<EquipmentResponseDto> getAllEquipments();
    void deleteEquipment(Long id);

}
