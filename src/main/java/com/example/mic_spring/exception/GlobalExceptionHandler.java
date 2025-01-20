package com.example.mic_spring.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.mic_spring.domain.dto.ApiResponse;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntimeException(RuntimeException e) {
        ApiResponse<Object> response = new ApiResponse<>(400, false, e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Object>> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse<Object> response = new ApiResponse<>(errorCode.getStatus().value(), false, errorCode.getMessage(), null);
        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }
}