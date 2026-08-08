package com.rockskay.backend.otp.dto;

import com.rockskay.backend.otp.constant.OtpChannel;
import com.rockskay.backend.otp.constant.OtpPurpose;

public record OtpSendResponse(
        OtpPurpose purpose,
        OtpChannel channel,
        int expiresIn
) {
}