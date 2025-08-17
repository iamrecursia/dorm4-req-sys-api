package com.kozitskiy.dorm4.sys.service;

import com.kozitskiy.dorm4.sys.entities.Dormitory;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DormitoryService {
    Dormitory createDorm(Dormitory dormitory);
    Dormitory updateDorm(Long dormId, Dormitory dormitory);
    void deleteDorm(Long dormId);
    List<Dormitory> findAllDormitory();
}
