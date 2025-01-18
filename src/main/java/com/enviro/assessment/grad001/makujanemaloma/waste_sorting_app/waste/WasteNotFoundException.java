package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.waste;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WasteNotFoundException extends RuntimeException {
    public WasteNotFoundException( String message ) {
        super( message );
    }
}
