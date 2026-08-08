package com.rockskay.backend.otp.exception;

public class OtpRateLimitExceededException extends RuntimeException {

    public OtpRateLimitExceededException() {
        super("OTP request limit exceeded");
    }
}
