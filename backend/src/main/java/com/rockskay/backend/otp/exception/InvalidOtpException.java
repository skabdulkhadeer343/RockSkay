package com.rockskay.backend.otp.exception;

import lombok.Getter;

@Getter
public class InvalidOtpException extends RuntimeException{

    public InvalidOtpException(String message) {
        super(message);
    }
}
