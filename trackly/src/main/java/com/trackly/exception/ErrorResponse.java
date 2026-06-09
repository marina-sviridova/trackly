package com.trackly.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse {
    String message;
    int status;
    LocalDateTime timestamp;
}