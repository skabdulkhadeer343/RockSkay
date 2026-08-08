package com.rockskay.backend.auth.exceptions;

import lombok.Getter;

@Getter
public class TokenExpiredException extends RuntimeException{


    public TokenExpiredException(String message) {
        super(message);
    }
}
