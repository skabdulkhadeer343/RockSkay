package com.rockskay.backend.auth.exceptions;

import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException{


    public UnauthorizedException(String message) {
        super(message);
    }
}
