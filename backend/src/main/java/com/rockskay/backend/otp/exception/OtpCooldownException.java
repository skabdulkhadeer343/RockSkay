package com.rockskay.backend.otp.exception;

public class OtpCooldownException extends RuntimeException {

    public OtpCooldownException() {
        super("Please wait before requesting another OTP");
    }
}
