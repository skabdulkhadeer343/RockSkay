
package com.rockskay.backend.otp.email;

import com.rockskay.backend.otp.constant.OtpPurpose;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class OtpEmailTemplate {

    public EmailContent create(
            OtpPurpose purpose,
            String otp,
            Duration expiry
    ) {

        return switch (purpose) {

            case EMAIL_VERIFICATION -> new EmailContent(
                    "Welcome to RockSkAy — Verify your email",
                    "Welcome to RockSkAy!",
                    "Thanks for creating your account. Please verify your email address using the code below.",
                    otp,
                    expiry
            );

            case PASSWORD_RESET -> new EmailContent(
                    "Reset your RockSkAy password",
                    "Reset your password",
                    "We received a request to reset your RockSkAy password. Use the verification code below to continue.",
                    otp,
                    expiry
            );

            case RESUME_DOWNLOAD -> new EmailContent(
                    "Verify your identity — RockSkAy",
                    "Verify your identity",
                    "Please use the verification code below to verify your identity and continue with your resume download.",
                    otp,
                    expiry
            );

            case ACCOUNT_DELETION -> new EmailContent(
                    "Confirm your RockSkAy account deletion",
                    "Confirm account deletion",
                    "You requested to delete your RockSkAy account. Use the verification code below to confirm this action.",
                    otp,
                    expiry
            );
        };
    }

    public record EmailContent(
            String subject,
            String title,
            String message,
            String otp,
            Duration expiry
    ) {
    }
}