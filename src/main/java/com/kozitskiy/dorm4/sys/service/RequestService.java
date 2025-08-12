package com.kozitskiy.dorm4.sys.service;

import com.kozitskiy.dorm4.sys.dto.RequestCreateDto;
import com.kozitskiy.dorm4.sys.dto.RequestResponseDto;
import com.kozitskiy.dorm4.sys.dto.RequestUpdateDto;
import com.kozitskiy.dorm4.sys.dto.WorkerRequestDto;
import com.kozitskiy.dorm4.sys.entities.Request;
import com.kozitskiy.dorm4.sys.entities.enums.RequestStatus;
import com.kozitskiy.dorm4.sys.entities.enums.RequestType;

import java.util.List;

public interface RequestService {
    RequestResponseDto createRequest(RequestCreateDto requestCreateDto);
    Request updateRequestByWorker(RequestUpdateDto requestUpdateDto);
    List<WorkerRequestDto> getRequestByWorkerIdAndType(Long workerId, RequestType type);
    void deleteRequestByRequestId(Long requestId);


//    Request updateRequest(Long requestId, RequestStatus requestStatus, Long studentId);
//    List<Request> getAllRequestsForWorkers(Long workerId);
//    List<Request> getAllRequestsForStudent(Long studentId);
//    List<Request> getAllRequests();
//    Request assignRequestToWorker(Long requestId, Long workerId);

}
