package com.rockskay.backend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EmailVerificationOtpVerifyRequest(

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Pattern(
                regexp = "\\d{6}",
                message = "OTP must be exactly 6 digits"
        )
        String otp

) {
}