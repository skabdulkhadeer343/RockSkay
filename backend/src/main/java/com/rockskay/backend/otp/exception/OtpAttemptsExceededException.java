package com.rockskay.backend.otp.exception;

public class OtpAttemptsExceededException extends RuntimeException {

    public OtpAttemptsExceededException() {
        super("Maximum OTP verification attempts exceeded");
    }
}
