package com.kozitskiy.dorm4.sys.dto.equipment;

import lombok.Builder;

@Builder
public record EquipmentResponseDto(
        Long id,
        String name,
        String description,
        String status
) {
}
