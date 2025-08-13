package com.kozitskiy.dorm4.sys.service;

import com.kozitskiy.dorm4.sys.entities.Dormitory;
import com.kozitskiy.dorm4.sys.entities.Room;
import com.kozitskiy.dorm4.sys.exceptions.DormNotFoundException;
import com.kozitskiy.dorm4.sys.repositories.DormitoryRepository;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class DormitoryServiceImpl implements DormitoryService {
    private final DormitoryRepository dormitoryRepository;

    @Override
    public Dormitory createDorm(Dormitory dormitory) {
        Dormitory createdDorm = Dormitory.builder()
                .name(dormitory.getName())
                .address(dormitory.getAddress())
                .floorsCount(dormitory.getFloorsCount())
                .rooms(dormitory.getRooms())
                .build();

        return dormitoryRepository.save(createdDorm);
    }

    @Transactional
    @Override
    public Dormitory updateDorm(Long dormId, Dormitory dormitory) {
        Dormitory requiredDorm = dormitoryRepository.findById(dormId)
                .orElseThrow(() -> new DormNotFoundException("Dorm with id = " + dormId + " wasn't found"));

        requiredDorm.setName(dormitory.getName());
        requiredDorm.setAddress(dormitory.getAddress());
        requiredDorm.setFloorsCount(dormitory.getFloorsCount());

        if (requiredDorm.getRooms().isEmpty()){
            requiredDorm.setRooms(requiredDorm.getRooms());
        }

        requiredDorm.getRooms().clear();
        for (Room room : requiredDorm.getRooms()){
            room.setDormitory(requiredDorm);
            requiredDorm.getRooms().add(room);
        }

        return dormitoryRepository.save(requiredDorm);
    }

    @Override
    public ResponseEntity<Dormitory> deleteDorm(Long dormId) {
        dormitoryRepository.deleteById(dormId);
        return null;
    }

    @Transactional
    @Override
    public List<Dormitory> findAllDormitory() {
        return dormitoryRepository.findAll();
    }
}
