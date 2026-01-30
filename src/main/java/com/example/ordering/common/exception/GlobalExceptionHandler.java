package com.example.ordering.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CommonErrorDto> handleCustomException(CustomException e) {

        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(
                        CommonErrorDto.builder()
                                .statusCode(errorCode.getStatus().value())
                                .errorCode(errorCode.name())
                                .errorMessage(errorCode.getMessage())
                                .timestamp(LocalDateTime.now())
                                .build()
                );
    }

}