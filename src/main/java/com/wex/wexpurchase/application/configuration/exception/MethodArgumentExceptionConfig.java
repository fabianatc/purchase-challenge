package com.wex.wexpurchase.application.configuration.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class MethodArgumentExceptionConfig {

    /**
     * Handles MethodArgumentNotValidException and returns a ResponseEntity with validation errors.
     *
     * @param ex The MethodArgumentNotValidException instance.
     * @return ResponseEntity containing the validation errors.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        // Extract field errors from the exception and map them to error messages
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());

        // Create a ResponseEntity with the error map and HTTP status BAD_REQUEST
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Creates a map containing validation errors under the "errors" key.
     *
     * @param errors The list of validation error messages.
     * @return Map containing validation errors.
     */
    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors); // Put the errors list under the "errors" key
        return errorResponse;
    }
}

