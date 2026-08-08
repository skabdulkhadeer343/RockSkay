package com.rockskay.backend.otp.dto;

import com.rockskay.backend.otp.constant.OtpChannel;
import com.rockskay.backend.otp.constant.OtpPurpose;


public record OtpSendRequest(
        String userId,
        String destination,
        OtpPurpose purpose,
        OtpChannel channel
) {
}