package com.kozitskiy.dorm4.sys.dto;

import lombok.Builder;

@Builder
public record EquipmentResponseDto(
        Long id,
        String name,
        String description,
        String status,
        Long roomId
) {
}
