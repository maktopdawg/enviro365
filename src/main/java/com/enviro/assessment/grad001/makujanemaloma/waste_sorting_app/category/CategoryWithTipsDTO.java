package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.category;

import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.recycling.RecyclingTipDTO;

import java.util.List;

public record CategoryWithTipsDTO(
        Integer id,
        String name,
        String description,
        List<RecyclingTipDTO> recyclingTips
) {
}
