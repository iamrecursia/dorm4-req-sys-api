package com.kozitskiy.dorm4.sys.service;

import com.kozitskiy.dorm4.sys.entities.Request;
import com.kozitskiy.dorm4.sys.entities.User;
import com.kozitskiy.dorm4.sys.entities.enums.RequestStatus;
import com.kozitskiy.dorm4.sys.repositories.RequestRepository;
import com.kozitskiy.dorm4.sys.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;


    @Override
    public Request createRequest(Request request, String studentUsername) {
        User student = userRepository.findByUsername(studentUsername)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Request savedRequest = Request.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .student(student)
                .createdAt(LocalDateTime.now())
                .build();

        return requestRepository.save(savedRequest);
    }

    @Override
    public Request updateRequest(Long requestId, RequestStatus requestStatus, String workerUsername) {
        User worker = userRepository.findByUsername(workerUsername)
                .orElseThrow(() -> new RuntimeException("Worker not found"));

        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if(!request.getWorker().equals(worker)) {
            throw new RuntimeException("You can only update ur own requests");
        }

        request.setStatus(requestStatus);
        request.setUpdatedAt(LocalDateTime.now());
        return requestRepository.save(request);

    }

    @Override
    public List<Request> getAllRequestsForWorkers(String workerUsername) {
        User worker = userRepository.findByUsername(workerUsername)
                .orElseThrow(() -> new RuntimeException("Worker not found"));

        return requestRepository.findByWorker(worker);

    }

    @Override
    public List<Request> getAllRequestsForStudent(String studentUsername) {
        User student = userRepository.findByUsername(studentUsername)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return requestRepository.findByStudent(student);
    }

    @Override
    public List<Request> getAllRequests() {
        return requestRepository.findAll();
    }

    @Override
    public Request assignRequestToWorker(Long requestId, String workerUsername) {
        User worker = userRepository.findByUsername(workerUsername)
                .orElseThrow(() -> new RuntimeException("Worker not found"));

        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if(!request.getRequestType().name().equals(worker.getUserType().name())) {
            throw new RuntimeException("Request type and worker type mismatch");
        }

        request.setWorker(worker);
        request.setStatus(RequestStatus.IN_PROGRESS);
        return requestRepository.save(request);
    }
}
