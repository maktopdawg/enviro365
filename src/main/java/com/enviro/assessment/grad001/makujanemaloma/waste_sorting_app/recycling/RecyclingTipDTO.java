package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.recycling;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RecyclingTipDTO(
        Integer id,

        @NotNull( message = "title must not be empty")
        String title,

        @NotNull( message = "title must not be empty")
        String tip,
        Integer categoryId,
        Integer wasteId,
        LocalDateTime lastUpdated
) {
}
