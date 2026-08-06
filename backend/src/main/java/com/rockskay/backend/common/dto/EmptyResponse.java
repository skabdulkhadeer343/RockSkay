
package com.rockskay.backend.common.dto;

import java.time.Instant;

public record EmptyResponse (
        boolean success,
        String message,
        Instant timestramp
){

    public static EmptyResponse of(String message) {
        return new EmptyResponse(true, message, Instant.now());
    }

}