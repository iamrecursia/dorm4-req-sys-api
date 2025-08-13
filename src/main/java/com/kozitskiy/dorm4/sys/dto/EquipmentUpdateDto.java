package com.kozitskiy.dorm4.sys.dto;

import lombok.Builder;

@Builder
public record EquipmentUpdateDto(
        String name,
        String description,
        String status
) { }
