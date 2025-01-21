package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.category.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException( String message ) {
        super( message );
    }
}
