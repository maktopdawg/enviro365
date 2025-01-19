package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.waste;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record WasteDTO(
        Integer id,

        @NotNull( message = "Waste name cannot be null")
        String name,

        @NotNull( message = "Waste description cannot be null")
        @Size( min=10, max=200, message = "Waste Description must be between 10 and 200 characters")
        String description,

        @NotNull( message = "Waste Category ID cannot be null")
        Integer categoryId,
        LocalDateTime lastUpdated
) {

}
