package com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.exception;

import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.category.exceptions.CategoryNotFoundException;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.disposal.exceptions.DisposalNotFoundException;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.recycling.exceptions.RecyclingTipNotFound;
import com.enviro.assessment.grad001.makujanemaloma.waste_sorting_app.waste.exceptions.WasteNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation exceptions.
     */
    @ExceptionHandler( MethodArgumentNotValidException.class )
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex
    ) {
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
     * Handles `MethodArgumentTypeMismatchException`.
     */
    @ExceptionHandler( MethodArgumentTypeMismatchException.class )
    public ResponseEntity<Map<String, Object>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request
    ) {
        String errorMessage = String.format( "Invalid value '%s' for parameter '%s'. Expected type: %s",
                ex.getValue(),
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown"
        );

        return buildErrorResponse(
                "Method Argument Type Mismatch",
                HttpStatus.BAD_REQUEST,
                errorMessage,
                request.getRequestURI()
        );
    }

    /**
     * Handles `HttpRequestMethodNotSupportedException`.
     */
    @ExceptionHandler( HttpRequestMethodNotSupportedException.class )
    public ResponseEntity<Map<String, Object>> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request
    ) {
        String errorMessage = String.format( "The HTTP method '%s' is not supported for this endpoint. Supported methods are: %s",
                ex.getMethod(),
                ex.getSupportedHttpMethods() != null ? ex.getSupportedHttpMethods() : "unknown" );

        return buildErrorResponse(
                "Method Not Allowed",
                HttpStatus.METHOD_NOT_ALLOWED,
                errorMessage,
                request.getRequestURI()
        );
    }

    /**
     * Handles `ConstraintViolationException`.
     */
    @ExceptionHandler( ConstraintViolationException.class )
    public ResponseEntity<Map<String, Object>> handleConstraintViolationException( ConstraintViolationException ex ) {
        Map<String, String> errors = new HashMap<>();
        for ( ConstraintViolation<?> violation : ex.getConstraintViolations() ) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put( fieldName, errorMessage );
        }

        return buildErrorResponse(
                "Constraint Violations",
                HttpStatus.BAD_REQUEST,
                errors
        );
    }

    /**
     * Handles `DuplicateKeyException`.
     */
    @ExceptionHandler( DuplicateKeyException.class )
    public ResponseEntity<Map<String, Object>> handleDuplicateKeyException(
            DuplicateKeyException ex,
            HttpServletRequest request
    ) {
        String errorMessage = "A record with the same key already exists.";

        return buildErrorResponse(
                "Duplicate Key Error",
                HttpStatus.CONFLICT,
                errorMessage,
                request.getRequestURI()
        );
    }

    /**
     * Handles `NoResourceFoundException`.
     */
    @ExceptionHandler( NoResourceFoundException.class )
    public ResponseEntity<Map<String, Object>> handleNoResourceFoundException(
            NoResourceFoundException ex,
            HttpServletRequest request
    ) {
        return buildErrorResponse(
                "Resource Not Found",
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI()
        );
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
