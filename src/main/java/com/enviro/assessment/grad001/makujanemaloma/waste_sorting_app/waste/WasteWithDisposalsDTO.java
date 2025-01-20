package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.waste;

import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.disposal.DisposalDTO;

import java.util.List;

public record WasteWithDisposalsDTO(
        Integer id,
        String name,
        String description,
        String category,
        List<DisposalDTO> disposals
) {

}