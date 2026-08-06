package com.rockskay.backend.common.dto;

import java.time.Instant;
import java.util.List;

public record ErrorResponse (

    boolean success,
    int status,
    String message,
    List<String> errors,
    Instant timestamp
){
    private ErrorResponse(
            int status,
            String message,
            List<String> errors
    ) {
        this(false, status, message, errors, Instant.now());
    }


    public static ErrorResponse of(
            int status,
            String message
    ) {
        return new ErrorResponse(
                status,
                message,
                List.of()
        );
    }


    public static ErrorResponse of(
            int status,
            String message,
            List<String> errors
    ) {
        return new ErrorResponse(
                status,
                message,
                errors
        );
    }


    public static ErrorResponse of(
            int status,
            String message,
            String error
    ) {
        return new ErrorResponse(
                status,
                message,
                List.of(error)
        );
    }
}