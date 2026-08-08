package com.rockskay.backend.otp.dto;

import com.rockskay.backend.otp.constant.OtpChannel;
import com.rockskay.backend.otp.constant.OtpPurpose;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

public record OtpVerifyRequest(

        String userId,
        OtpPurpose purpose,
        OtpChannel channel,
        @NotBlank
        @Pattern(regexp = "\\d{6}")
        String otp
) {
}
