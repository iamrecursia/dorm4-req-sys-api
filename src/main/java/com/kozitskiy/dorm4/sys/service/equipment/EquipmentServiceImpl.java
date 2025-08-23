package com.kozitskiy.dorm4.sys.service.equipment;

import com.kozitskiy.dorm4.sys.dto.equipment.EquipmentCreateDto;
import com.kozitskiy.dorm4.sys.dto.equipment.EquipmentResponseDto;
import com.kozitskiy.dorm4.sys.dto.equipment.EquipmentUpdateDto;
import com.kozitskiy.dorm4.sys.entities.Equipment;
import com.kozitskiy.dorm4.sys.exceptions.EquipmentNotFoundException;
import com.kozitskiy.dorm4.sys.mapper.EquipmentMapper;
import com.kozitskiy.dorm4.sys.repositories.EquipmentRepository;
import com.kozitskiy.dorm4.sys.repositories.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {
    private final EquipmentRepository equipmentRepository;
    private final EquipmentMapper equipmentMapper;
    private final RoomRepository roomRepository;

    @Override
    public EquipmentResponseDto createEquipment(EquipmentCreateDto dto) {

        Equipment createdEquipment = equipmentMapper.convertToEntity(dto);

        Equipment savedEquipment = equipmentRepository.save(createdEquipment);

        return equipmentMapper.convertToDto(savedEquipment);
    }

    @Override
    public EquipmentResponseDto updateEquipment(Long id, EquipmentUpdateDto equipmentUpdateDto) {
            Equipment equipment = equipmentRepository.findById(id)
                    .orElseThrow(() -> new EquipmentNotFoundException("Equipment doesn't exists"));

            equipmentMapper.updateEntityFromDto(equipmentUpdateDto, equipment);

            Equipment updatedEquipment = equipmentRepository.save(equipment);

            return equipmentMapper.convertToDto(updatedEquipment);
    }

    @Override
    @Transactional
    public List<EquipmentResponseDto> getAllEquipments() {
        List<Equipment> equipments = equipmentRepository.findAll();
        return equipments.stream()
                .map(equipmentMapper::convertToDto)
                .toList();
    }

    @Override
    public void deleteEquipment(Long id) {
        equipmentRepository.deleteById(id);
    }
}
