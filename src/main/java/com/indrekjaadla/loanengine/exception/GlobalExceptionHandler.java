package com.indrekjaadla.loanengine.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnsupportedLoanConfigurationException.class)
    public ResponseEntity<Map<String, Object>> handleUnsupportedLoanConfigurationException(UnsupportedLoanConfigurationException exception) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", BAD_REQUEST.value());
        body.put("error", "Unsupported Loan Configuration");
        body.put("message", exception.getMessage());

        return ResponseEntity.status(BAD_REQUEST).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException exception) {
        Map<String, List<String>> fieldErrors = new HashMap<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> fieldErrors
                        .computeIfAbsent(error.getField(), k -> new ArrayList<>()).add(error.getDefaultMessage()));

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", BAD_REQUEST.value());
        body.put("error", "Validation Failed");
        body.put("message", fieldErrors);

        return ResponseEntity.status(BAD_REQUEST).body(body);
    }
}
