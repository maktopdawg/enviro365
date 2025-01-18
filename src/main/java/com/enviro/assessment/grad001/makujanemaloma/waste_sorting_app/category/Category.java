package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record Category(
        Integer id,

        @NotNull( message = "Name must not be empty")
        @Size( min=3, max=100, message = "Name must be between 3 and 100 characters")
        String name,

        @NotNull( message = "Description must not be empty")
        @Size( min=10, max=200, message = "Description must be between 10 and 200 characters")
        String description,
        LocalDateTime lastUpdated
) {
    public Category {

    }
}
