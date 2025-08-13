package com.kozitskiy.dorm4.sys.repositories;

import com.kozitskiy.dorm4.sys.entities.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
}
