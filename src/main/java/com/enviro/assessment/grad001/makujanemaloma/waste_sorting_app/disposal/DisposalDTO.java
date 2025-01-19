package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.disposal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


import java.time.LocalDateTime;

public record DisposalDTO(
        Integer id,

        @NotNull( message = "Waste ID cannot be null")
        Integer wasteId,

        @NotNull( message = "Disposal method cannot be null")
        String method,

        @NotNull( message = "Disposal instructions cannot be null")
        @Size( min=10, max = 255, message = "Disposal instructions must be between 10 and 255 characters")
        String instructions,

        @NotNull( message = "Disposal location cannot be null")
        String location,
        LocalDateTime lastUpdated
) {

}
