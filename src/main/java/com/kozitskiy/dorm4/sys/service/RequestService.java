package com.kozitskiy.dorm4.sys.service;

import com.kozitskiy.dorm4.sys.entities.Request;
import com.kozitskiy.dorm4.sys.entities.enums.RequestStatus;

import java.util.List;

public interface RequestService {
    Request createRequest(Request request, String studentUsername);
    Request updateRequest(Long requestId, RequestStatus requestStatus, String studentUsername);
    List<Request> getAllRequestsForWorkers(String workerUsername);
    List<Request> getAllRequestsForStudent(String studentUsername);
    List<Request> getAllRequests();
    Request assignRequestToWorker(Long requestId, String workerUsername);

}
