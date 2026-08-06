package com.rockskay.backend.common.exception.auth;

import lombok.Getter;

@Getter
public class InvalidTokenException extends RuntimeException{


    public InvalidTokenException(String message) {
        super(message);
    }
}
