package com.kozitskiy.dorm4.sys.repositories;

import com.kozitskiy.dorm4.sys.entities.Dormitory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DormitoryRepository extends JpaRepository<Dormitory, Long> {
}
