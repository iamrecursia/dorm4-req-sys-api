package com.kozitskiy.dorm4.sys.service.dorm;

import com.kozitskiy.dorm4.sys.dto.dorm.DormitoryCreateDto;
import com.kozitskiy.dorm4.sys.dto.dorm.DormitoryResponseDto;
import com.kozitskiy.dorm4.sys.dto.dorm.DormitoryUpdateDto;
import com.kozitskiy.dorm4.sys.entities.Dormitory;
import com.kozitskiy.dorm4.sys.exceptions.DormNotFoundException;
import com.kozitskiy.dorm4.sys.repositories.DormitoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DormitoryServiceImpl implements DormitoryService {
    private final DormitoryRepository dormitoryRepository;

    @Override
    public DormitoryResponseDto createDorm(DormitoryCreateDto dto) {
        Dormitory dormitory = Dormitory.builder()
                .name(dto.name())
                .address(dto.address())
                .floorsCount(dto.floorsCound())
                .rooms(new ArrayList<>())
                .build();

        dormitoryRepository.save(dormitory);

        return DormitoryResponseDto.builder()
                .id(dormitory.getId())
                .name(dormitory.getName())
                .address(dormitory.getAddress())
                .floorsCount(dormitory.getFloorsCount())
                .build();
    }

    @Transactional
    @Override
    public DormitoryResponseDto updateDorm(Long dormId, DormitoryUpdateDto dto) {
        Dormitory dormitory = dormitoryRepository.findById(dormId)
                .orElseThrow(() -> new DormNotFoundException("Dormitory with id " + dormId + " not found"));

        if (dto.name() != null && !dto.name().isBlank()){
            dormitory.setName(dto.name());
        }

        Dormitory updatedDormitory = dormitoryRepository.save(dormitory);

        return DormitoryResponseDto.builder()
                .id(updatedDormitory.getId())
                .name(updatedDormitory.getName())
                .address(updatedDormitory.getAddress())
                .floorsCount(updatedDormitory.getFloorsCount())
                .build();
    }

    @Override
    public void deleteDorm(Long dormId) {
        dormitoryRepository.deleteById(dormId);
    }

    @Transactional
    @Override
    public List<DormitoryResponseDto> findAllDormitory() {
        return dormitoryRepository.findAll().stream()
                .map(dormitory -> DormitoryResponseDto.builder()
                        .id(dormitory.getId())
                        .name(dormitory.getName())
                        .address(dormitory.getAddress())
                        .floorsCount(dormitory.getFloorsCount())
                        .build())
                .toList();
    }
}
