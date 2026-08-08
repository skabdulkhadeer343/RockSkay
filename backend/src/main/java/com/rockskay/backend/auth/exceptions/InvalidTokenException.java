package com.rockskay.backend.auth.exceptions;

import lombok.Getter;

@Getter
public class InvalidTokenException extends RuntimeException{


    public InvalidTokenException(String message) {
        super(message);
    }
}
