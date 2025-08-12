package com.kozitskiy.dorm4.sys.service;

import com.kozitskiy.dorm4.sys.dto.RequestCreateDto;
import com.kozitskiy.dorm4.sys.dto.RequestResponseDto;
import com.kozitskiy.dorm4.sys.dto.RequestUpdateDto;
import com.kozitskiy.dorm4.sys.dto.WorkerRequestDto;
import com.kozitskiy.dorm4.sys.entities.Request;
import com.kozitskiy.dorm4.sys.entities.User;
import com.kozitskiy.dorm4.sys.entities.enums.RequestStatus;
import com.kozitskiy.dorm4.sys.entities.enums.RequestType;
import com.kozitskiy.dorm4.sys.entities.enums.UserType;
import com.kozitskiy.dorm4.sys.exceptions.RequestNotFoundException;
import com.kozitskiy.dorm4.sys.exceptions.UserNotFoundException;
import com.kozitskiy.dorm4.sys.mapper.RequestMapper;
import com.kozitskiy.dorm4.sys.repositories.RequestRepository;
import com.kozitskiy.dorm4.sys.repositories.UserRepository;
import com.kozitskiy.dorm4.sys.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final SecurityService securityService;
    private final RequestMapper requestMapper;

    //Проверить этот метод, а именно мапинг
    @PreAuthorize("hasAuthority('STUDENT') or hasAnyAuthority('ADMIN')")
    @Override
    public RequestResponseDto createRequest(RequestCreateDto dto) {

        Long currentUserId = securityService.getCurrentUserId();

        // Find request author
        User student = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException("Student not found"));

        if(!securityService.isCurrentUserAdmin() &&
        !student.getUserType().equals(UserType.STUDENT)) {
            throw new AccessDeniedException("You do not have permission to access this resource");

        }else if(!student.getUserType().equals(UserType.STUDENT)){
            throw new UserNotFoundException("Only students can create requests");
        }

        // Create request
        Request request = Request.builder()
                .title(dto.title())
                .description(dto.description())
                .requestType(dto.requestType())
                .student(student)
                .status(RequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        User worker = findAvailableWorkerByRequestType(dto.requestType());
        request.setWorker(worker);
        log.info("Request assigned to worker: {}", worker.getId());

        Request savedRequest = requestRepository.save(request);

        return RequestResponseDto.builder()
                .id(savedRequest.getId())
                .title(savedRequest.getTitle())
                .description(savedRequest.getDescription())
                .requestType(savedRequest.getRequestType())
                .status(savedRequest.getStatus())
                .createdAt(savedRequest.getCreatedAt())
                .build();

    }


    @PreAuthorize("hasAuthority('PLUMBER') or hasAuthority('ELECTRICIAN') or hasAuthority('ADMIN')")
    @Override
    public Request updateRequestByWorker(RequestUpdateDto dto) {
        Request request = requestRepository.findById(dto.requestId())
                .orElseThrow(() -> new RequestNotFoundException("Request not found with id = " + dto.requestId()));

        User worker = userRepository.findById(dto.workerId())
                .orElseThrow(() -> new UserNotFoundException("Worker not found with id = " + dto.workerId()));

        // Check if user has worker Role
        if(!worker.getUserType().equals(UserType.ELECTRICIAN) &&
                !worker.getUserType().equals(UserType.PLUMBER)) {
            throw new IllegalStateException("User is not a worker");
        }

        // Check request and worker match
        if (dto.newRequestType() != null) {
            if ((dto.newRequestType() == RequestType.PLUMBER &&
                    worker.getUserType() != UserType.PLUMBER) ||
                    (dto.newRequestType() == RequestType.ELECTRICIAN &&
                            worker.getUserType() != UserType.ELECTRICIAN)) {
                throw new IllegalStateException("Worker type doesn't match request type");
            }
            request.setRequestType(dto.newRequestType());
        }

        if (dto.newStatus() != null) {
            request.setStatus(dto.newStatus());
        }

        request.setWorker(worker);
        request.setUpdatedAt(LocalDateTime.now());

        return requestRepository.save(request);

    }

    @PreAuthorize("hasAuthority('PLUMBER') or hasAuthority('ELECTRICIAN') or hasAuthority('ADMIN')")
    @Override
    public List<WorkerRequestDto> getRequestByWorkerIdAndType(Long workerId, RequestType requestType) {
        log.info("Fetching all requests for worker ID: {}", workerId);

        return requestRepository.findByWorkerId(workerId).stream()
                .filter(request -> request.getRequestType() == requestType)
                .map(this::converToWorkerRequestDto)
                .toList();

    }//

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public void deleteRequestByRequestId(Long requestId) {
        requestRepository.deleteRequestById(requestId);
    }

    public WorkerRequestDto converToWorkerRequestDto(Request request) {
        return WorkerRequestDto.builder()
                .id(request.getId())
                .title(request.getTitle())
                .status(request.getStatus())
                .createdAt(request.getCreatedAt())
                .build();
    }

    // Find first available worker with needed request type
    private User findAvailableWorkerByRequestType(RequestType requestType) {

        UserType workerType = mapRequestTypeToWorkerType(requestType);
        List<User> availableWorkers = userRepository.findByUserType(workerType);

        if (availableWorkers.isEmpty()) {
            throw new UserNotFoundException("Worker not found");
        }

        return availableWorkers.get(0);
    }

    private UserType mapRequestTypeToWorkerType(RequestType requestType) {
        return switch (requestType) {
            case ELECTRICIAN -> UserType.ELECTRICIAN;
            case PLUMBER -> UserType.PLUMBER;
        };
    }


//    @Override
//    public Request updateRequest(Long requestId, RequestStatus requestStatus, Long studentId) {
//        User worker = userRepository.findById(studentId)
//                .orElseThrow(() -> new UserNotFoundException("User not found"));
//
//        Request request = requestRepository.findById(requestId)
//                .orElseThrow(() -> new RuntimeException("Request not found"));
//
//        if(!request.getWorker().equals(worker)) {
//            throw new RuntimeException("You can only update ur own requests");
//        }
//
//        request.setStatus(requestStatus);
//        request.setUpdatedAt(LocalDateTime.now());
//        return requestRepository.save(request);
//
//    }
//
//    @Override
//    public List<Request> getAllRequestsForWorkers(Long workerId) {
//        User worker = userRepository.findById(workerId)
//                .orElseThrow(() -> new UserNotFoundException("User not found"));
//
//        return requestRepository.findByWorker(worker);
//
//    }
//
//    @Override
//    public List<Request> getAllRequestsForStudent(Long studentId) {
//        User student = userRepository.findById(studentId)
//                .orElseThrow(() -> new UserNotFoundException("User not found"));
//
//        return requestRepository.findByStudent(student);
//    }
//
//    @Override
//    public List<Request> getAllRequests() {
//        return requestRepository.findAll();
//    }
//

}
