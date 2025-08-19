package com.kozitskiy.dorm4.sys.service.request;

import com.kozitskiy.dorm4.sys.dto.request.RequestCreateDto;
import com.kozitskiy.dorm4.sys.dto.request.RequestResponseDto;
import com.kozitskiy.dorm4.sys.dto.request.RequestUpdateDto;
import com.kozitskiy.dorm4.sys.dto.user.WorkerRequestDto;
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
    private final RequestAccessService requestAccessService;

    //Проверить этот метод, а именно мапинг
    @Override
    public RequestResponseDto createRequest(RequestCreateDto dto) {

        Long currentUserId = securityService.getCurrentUserId();

        requestAccessService.validateRequestCreationRights(currentUserId);


        // Find request author
        User student = userRepository.findById(currentUserId)
                .orElseThrow(() -> new UserNotFoundException("Student not found"));

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

        return requestMapper.toRequestResponseDto(savedRequest);
    }

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

    @Override
    public List<WorkerRequestDto> getRequestByWorkerIdAndType(Long workerId, RequestType requestType) {
        log.info("Fetching all requests for worker ID: {}", workerId);

        return requestRepository.findByWorkerId(workerId).stream()
                .filter(request -> request.getRequestType() == requestType)
                .map(this::converToWorkerRequestDto)
                .toList();

    }//

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
}
