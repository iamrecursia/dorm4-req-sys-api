package com.kozitskiy.dorm4.sys.dto.dorm;

import lombok.Builder;

@Builder
public record DormitoryCreateDto(
        String name,
        String address,
        Integer floorsCound
) {
}
