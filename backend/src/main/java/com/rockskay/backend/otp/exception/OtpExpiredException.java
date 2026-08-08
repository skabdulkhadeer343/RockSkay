package com.rockskay.backend.otp.exception;

import lombok.Getter;

@Getter
public class OtpExpiredException extends RuntimeException{

    public OtpExpiredException(String message) {
        super(message);
    }
}

