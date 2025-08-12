package com.kozitskiy.dorm4.sys.controllers;

import com.kozitskiy.dorm4.sys.dto.RequestCreateDto;
import com.kozitskiy.dorm4.sys.dto.RequestResponseDto;
import com.kozitskiy.dorm4.sys.dto.RequestUpdateDto;
import com.kozitskiy.dorm4.sys.dto.WorkerRequestDto;
import com.kozitskiy.dorm4.sys.entities.Request;
import com.kozitskiy.dorm4.sys.entities.enums.RequestType;
import com.kozitskiy.dorm4.sys.service.RequestService;
import com.kozitskiy.dorm4.sys.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/request")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;
    private final UserService userService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('STUDENT') or hasAuthority('ADMIN')")
    public ResponseEntity<RequestResponseDto> createRequest(@RequestBody @Valid RequestCreateDto dto) {
        RequestResponseDto created = requestService.createRequest(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/worker-update")
    @PreAuthorize("hasAuthority('PLUMBER') or hasAuthority('ELECTRICIAN') or hasAuthority('ADMIN')")
    public ResponseEntity<Request> updateRequest(@RequestBody RequestUpdateDto dto) {
        return ResponseEntity.ok(requestService.updateRequestByWorker(dto));
    }

    @GetMapping("/{workerId}/by-type/{requestType}")
    @PreAuthorize("hasAuthority('PLUMBER') or hasAuthority('ELECTRICIAN')")
    public ResponseEntity<List<WorkerRequestDto>> getWorkerRequests(
            @PathVariable Long workerId, @PathVariable RequestType requestType) {

            return ResponseEntity.ok(requestService.getRequestByWorkerIdAndType(workerId, requestType));
    }

    @DeleteMapping("/{requestId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Request> deleteRequest(@PathVariable Long requestId) {
        requestService.deleteRequestByRequestId(requestId);
        return ResponseEntity.noContent().build();
    }




}
