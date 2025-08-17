package com.kozitskiy.dorm4.sys.controllers;

import com.kozitskiy.dorm4.sys.dto.RequestCreateDto;
import com.kozitskiy.dorm4.sys.dto.RequestResponseDto;
import com.kozitskiy.dorm4.sys.dto.RequestUpdateDto;
import com.kozitskiy.dorm4.sys.dto.WorkerRequestDto;
import com.kozitskiy.dorm4.sys.entities.Request;
import com.kozitskiy.dorm4.sys.entities.enums.RequestType;
import com.kozitskiy.dorm4.sys.service.RequestService;
import com.kozitskiy.dorm4.sys.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/requests")
@RequiredArgsConstructor
@Tag(name = "Request Management", description = "API for request management")
public class RequestController {
    private final RequestService requestService;
    private final UserService userService;

    @PostMapping()
    @Operation(summary = "Create request")
    @PreAuthorize("hasAuthority('STUDENT') or hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public ResponseEntity<RequestResponseDto> createRequest(@RequestBody @Valid RequestCreateDto dto) {
        RequestResponseDto created = requestService.createRequest(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}/worker")
    @Operation(summary = "Update request")
    @PreAuthorize("hasAuthority('PLUMBER') or hasAuthority('ELECTRICIAN') or hasAuthority('ADMIN')")
    public ResponseEntity<Request> updateRequest(@PathVariable Long id, @RequestBody RequestUpdateDto dto) {
        return ResponseEntity.ok(requestService.updateRequestByWorker(dto));
    }

    // find all worker requests
    @GetMapping("/workers/{id}")
    @Operation(summary = "Get worker request")
    @PreAuthorize("hasAuthority('PLUMBER') or hasAuthority('ELECTRICIAN')")
    public ResponseEntity<List<WorkerRequestDto>> getWorkerRequests(
            @PathVariable Long id, @RequestParam RequestType requestType) {

            return ResponseEntity.ok(requestService.getRequestByWorkerIdAndType(id, requestType));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete request")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Request> deleteRequest(@PathVariable Long id) {
        requestService.deleteRequestByRequestId(id);
        return ResponseEntity.noContent().build();
    }




}
