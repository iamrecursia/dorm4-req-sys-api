package com.kozitskiy.dorm4.sys.repositories;

import com.kozitskiy.dorm4.sys.dto.WorkerRequestDto;
import com.kozitskiy.dorm4.sys.entities.Request;
import com.kozitskiy.dorm4.sys.entities.User;
import com.kozitskiy.dorm4.sys.entities.enums.RequestType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByWorker(User worker);
    List<Request> findByStudent(User student);

    List<WorkerRequestDto> getRequestByWorkerIdAndRequestType(Long workerId, RequestType requestType);
    List<Request> findByWorkerId(Long workerId);
    void deleteRequestById(Long requestId);

}
