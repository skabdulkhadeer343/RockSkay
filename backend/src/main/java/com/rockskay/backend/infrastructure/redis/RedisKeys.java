package com.rockskay.backend.infrastructure.redis;

import com.rockskay.backend.otp.constant.OtpChannel;
import com.rockskay.backend.otp.constant.OtpPurpose;

import java.util.UUID;

public final class RedisKeys {

    private RedisKeys() {
    }

    public static String otp(
            String userId,
            OtpPurpose purpose,
            OtpChannel channel
    ) {
        return "otp:"
                + purpose
                + ":"
                + channel
                + ":"
                + userId;
    }

    public static String otpAttempts(
            String userId,
            OtpPurpose purpose,
            OtpChannel channel
    ) {
        return "otp:attempts:"
                + purpose
                + ":"
                + channel
                + ":"
                + userId;
    }

    public static String otpResendCooldown(
            String userId,
            OtpPurpose purpose,
            OtpChannel channel
    ) {
        return "otp:resend:"
                + purpose
                + ":"
                + channel
                + ":"
                + userId;
    }

    public static String otpRateLimit(
            String userId,
            OtpPurpose purpose,
            OtpChannel channel
    ) {
        return "otp:rate:"
                + purpose
                + ":"
                + channel
                + ":"
                + userId;
    }
}