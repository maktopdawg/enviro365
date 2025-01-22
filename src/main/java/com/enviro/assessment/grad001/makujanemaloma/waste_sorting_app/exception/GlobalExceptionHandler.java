package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.exception;

import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.category.exceptions.CategoryNotFoundException;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.disposal.exceptions.DisposalNotFoundException;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.recycling.exceptions.RecyclingTipNotFound;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.waste.exceptions.WasteNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation exceptions.
     */
    @ExceptionHandler( MethodArgumentNotValidException.class )
    public ResponseEntity<Map<String, Object>> handleValidationExceptions( MethodArgumentNotValidException ex ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ( ( FieldError ) error ).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put( fieldName, errorMessage );
        });

        return buildErrorResponse( "Validation Failed", HttpStatus.BAD_REQUEST, errors );
    }

    /**
     * Handles `CategoryNotFoundException`.
     */
    @ExceptionHandler( CategoryNotFoundException.class )
    public ResponseEntity<Map<String, Object>> handleCategoryNotFoundException( CategoryNotFoundException ex ) {
        return buildErrorResponse( "Category Not Found", HttpStatus.NOT_FOUND, ex.getMessage(),  "/api/categories" );
    }

    /**
     * Handles `DisposalNotFoundException`.
     */
    @ExceptionHandler( DisposalNotFoundException.class )
    public ResponseEntity<Map<String, Object>> handleDisposalNotFoundException( DisposalNotFoundException ex ) {
        return buildErrorResponse( "Disposal Not Found", HttpStatus.NOT_FOUND, ex.getMessage(), "/api/disposal" );
    }

    /**
     * Handles `WasteNotFoundException`.
     */
    @ExceptionHandler( WasteNotFoundException.class )
    public ResponseEntity<Map<String, Object>> handleWasteNotFoundException( WasteNotFoundException ex ) {
        return buildErrorResponse( "Waste Not Found", HttpStatus.NOT_FOUND, ex.getMessage(),  "/api/waste" );
    }

    /**
     * Handles `WasteNotFoundException`.
     */
    @ExceptionHandler( RecyclingTipNotFound.class )
    public ResponseEntity<Map<String, Object>> handleRecyclingTipNotFoundException( RecyclingTipNotFound ex ) {
        return buildErrorResponse( "Recycling Tip Not Found", HttpStatus.NOT_FOUND, ex.getMessage(),  "/api/recycling-tip" );
    }

    /**
     * Builds a standard error response for validation errors.
     */
    private ResponseEntity<Map<String, Object>> buildErrorResponse(
            String error,
            HttpStatus status,
            Map<String, String> errors
    ) {
        Map<String, Object> response = new HashMap<>();
        response.put( "timestamp", LocalDateTime.now() );
        response.put( "status", status.value() );
        response.put( "error", error );
        response.put( "errors", errors );
        return new ResponseEntity<>( response, status );
    }

    /**
     * Builds a standard error response for general exceptions.
     */
    private ResponseEntity<Map<String, Object>> buildErrorResponse(
            String error,
            HttpStatus status,
            String message,
            String path
    ) {
        Map<String, Object> response = new HashMap<>();
        response.put( "timestamp", LocalDateTime.now() );
        response.put( "status", status.value() );
        response.put( "error", error );
        response.put( "message", message );
        response.put( "path", path );
        return new ResponseEntity<>( response, status );
    }
}


// DuplicateKeyException