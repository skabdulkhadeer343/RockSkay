package com.rockskay.backend.otp.service;


import com.rockskay.backend.otp.constant.OtpChannel;
import com.rockskay.backend.otp.constant.OtpPurpose;

import java.time.Duration;

public interface OtpDeliveryService {

    void send(
            OtpChannel channel,
            String recipient,
            String otp,
            OtpPurpose purpose,
            Duration expiry
    );
}