package com.boot.stomp.rabbit.error.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.boot.stomp.rabbit.model.ErrorResponse;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity handleRuntimeException(RuntimeException e) {
        ErrorResponse err = new ErrorResponse();
        err.setError(e.getMessage());
        return ResponseEntity.ok(err);
    }
}
