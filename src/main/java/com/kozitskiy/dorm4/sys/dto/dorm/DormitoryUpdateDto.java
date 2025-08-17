package com.kozitskiy.dorm4.sys.dto.dorm;

import lombok.Builder;

@Builder
public record DormitoryUpdateDto(
        String name
) {
}
