package com.rockskay.backend.common.exception.auth;

import lombok.Getter;

@Getter
public class TokenExpiredException extends RuntimeException{


    public TokenExpiredException(String message) {
        super(message);
    }
}
