package com.rockskay.backend.otp.exception;


public class OtpNotFoundException extends RuntimeException {

    public OtpNotFoundException() {
        super("Invalid or expired OTP");
    }
}

