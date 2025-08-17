package com.kozitskiy.dorm4.sys.mapper;

import com.kozitskiy.dorm4.sys.dto.request.RequestResponseDto;
import com.kozitskiy.dorm4.sys.entities.Request;
import org.springframework.stereotype.Component;

@Component
public class RequestMapper {

    public RequestResponseDto toRequestResponseDto(Request dto) {
        return RequestResponseDto.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .createdAt(dto.getCreatedAt())
                .build();

    }

}
//    Long id,
//    String title,
//    String description,
//    RequestType requestType,
//    RequestStatus status,
//    UserDto student,  // Только нужные поля
//    UserDto worker,
//    LocalDateTime createdAt,
//    LocalDateTime updatedAt