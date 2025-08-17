package com.kozitskiy.dorm4.sys.dto.dorm;

import lombok.Builder;

@Builder
public record DormitoryResponseDto(
        Long id,
        String name,
        String address,
        Integer floorsCount
) {
}
