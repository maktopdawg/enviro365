package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.waste.models;

import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.recycling.RecyclingTipDTO;

import java.util.List;

public record WasteWithTipsDTO(
        Integer id,
        String name,
        String description,
        String categoryName,
        List<RecyclingTipDTO> recyclingTips
) {
}
