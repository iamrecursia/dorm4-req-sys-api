package com.kozitskiy.dorm4.sys.repositories;

import com.kozitskiy.dorm4.sys.entities.Request;
import com.kozitskiy.dorm4.sys.entities.User;
import com.kozitskiy.dorm4.sys.entities.enums.RequestType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByType(RequestType type);
    List<Request> findByWorker(User worker);
    List<Request> findByStudent(User student);
}
