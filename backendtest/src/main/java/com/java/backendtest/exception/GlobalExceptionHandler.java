package com.java.backendtest.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.java.backendtest.response.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(err ->
            errors.put(err.getField(), err.getDefaultMessage())
        );

        return ResponseEntity.badRequest()
                .body(new ApiResponse<>(
                		"Validation failed",
                		errors,
                		400,
                		Instant.now()));
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ApiResponse<Object>> handleStock(
            InsufficientStockException ex){

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiResponse<>(
                		ex.getMessage(),
                		null,
                		409,
                		Instant.now()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleEnumError(
            HttpMessageNotReadableException e) {
    	
    	log.warn("Enums validation failed: {}", e);

        return ResponseEntity.badRequest()
                .body(new ApiResponse<>(
                		"Invalid request body",
                		null,
                		400,
                		Instant.now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleUnknown(Exception ex){

        log.error("Unhandled exception occurred", ex);    	
    	
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(
                		"Internal server error",
                		null,
                		500,
                		Instant.now()));
    }
    
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFound(
            DataNotFoundException ex){
    	
    	log.info(ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(
                		ex.getMessage(),
                		null,
                		404,
                		Instant.now()));
    }
    
    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidSort(
            PropertyReferenceException ex){
    	
    	log.warn("Invalid sort field requested: {}", ex.getPropertyName());

        return ResponseEntity
                .badRequest()
                .body(new ApiResponse<>(
                    "Invalid sort field: " + ex.getPropertyName(),
                    null,
                    400,
                    Instant.now()
                ));
    }
}
