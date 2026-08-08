package com.rockskay.backend.otp.service;

import com.rockskay.backend.infrastructure.mail.MailService;
import com.rockskay.backend.otp.constant.OtpChannel;
import com.rockskay.backend.otp.constant.OtpPurpose;
import com.rockskay.backend.otp.email.OtpEmailRenderer;
import com.rockskay.backend.otp.email.OtpEmailTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class OtpDeliveryServiceImpl implements OtpDeliveryService {

    private final MailService mailService;
    private final OtpEmailTemplate otpEmailTemplate;
    private final OtpEmailRenderer otpEmailRenderer;

    @Override
    public void send(
            OtpChannel channel,
            String recipient,
            String otp,
            OtpPurpose purpose,
            Duration expiry
    ) {

        switch (channel) {

            case EMAIL -> sendEmail(
                    recipient,
                    otp,
                    purpose,
                    expiry
            );

            default -> throw new IllegalArgumentException(
                    "Unsupported OTP channel: " + channel
            );
        }
    }

    private void sendEmail(
            String recipient,
            String otp,
            OtpPurpose purpose,
            Duration expiry
    ) {

        OtpEmailTemplate.EmailContent content =
                otpEmailTemplate.create(
                        purpose,
                        otp,
                        expiry
                );

        String html =
                otpEmailRenderer.render(content);

        mailService.send(
                recipient,
                content.subject(),
                html
        );
    }
}