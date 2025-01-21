package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.waste;

public record WasteWithCategoryDTO(
        Integer wasteId,
        String wasteName,
        String wasteDescription,
        String categoryName,
        String categoryDescription
) {
}
