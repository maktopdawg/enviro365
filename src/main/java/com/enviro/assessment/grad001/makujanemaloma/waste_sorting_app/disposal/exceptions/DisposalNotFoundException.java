package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.disposal.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DisposalNotFoundException extends RuntimeException {
    public DisposalNotFoundException(String message) {
        super( message );
    }
}
