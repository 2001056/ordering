package com.example.ordering.common.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonErrorDto {

    private int statusCode;
    private String errorCode;
    private String errorMessage;
    private LocalDateTime timestamp;
}