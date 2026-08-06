package com.rockskay.backend.common.exception.auth;

import lombok.Getter;

@Getter
public class ForbiddenException extends RuntimeException{


    public ForbiddenException(String message) {
        super(message);
    }
}
