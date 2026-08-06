package com.rockskay.backend.common.dto;

import java.time.Instant;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        Instant timestamp
) {

    public static <T> ApiResponse<T> of(
            T data,
            String message
    ) {
        return new ApiResponse<>(
                true,
                message,
                data,
                Instant.now()
        );
    }
}