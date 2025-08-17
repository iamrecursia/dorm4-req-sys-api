package com.kozitskiy.dorm4.sys.dto.equipment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record EquipmentCreateDto(
        @NotBlank(message = "equipment name cannot be blank")
        String name,

        @NotBlank(message = "description cannot be blank")
        String description,

        @Pattern(regexp = "WORKING|REPAIR|DECOMMISSIONED",
                message = "Status must be WORKING, REPAIR or DECOMMISSIONED")
        String status

) { }
