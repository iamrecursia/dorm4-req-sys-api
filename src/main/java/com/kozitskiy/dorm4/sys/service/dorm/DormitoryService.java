package com.kozitskiy.dorm4.sys.service.dorm;

import com.kozitskiy.dorm4.sys.dto.dorm.DormitoryCreateDto;
import com.kozitskiy.dorm4.sys.dto.dorm.DormitoryResponseDto;
import com.kozitskiy.dorm4.sys.dto.dorm.DormitoryUpdateDto;

import java.util.List;

public interface DormitoryService {
    DormitoryResponseDto createDorm(DormitoryCreateDto dormitory);
    DormitoryResponseDto updateDorm(Long dormId, DormitoryUpdateDto dto);
    void deleteDorm(Long dormId);
    List<DormitoryResponseDto> findAllDormitory();
}
