package com.rockskay.backend.common.exception.auth;

import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException{


    public UnauthorizedException(String message) {
        super(message);
    }
}
