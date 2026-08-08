package com.rockskay.backend.otp.service;

import com.rockskay.backend.otp.constant.OtpChannel;
import com.rockskay.backend.otp.constant.OtpPurpose;
import com.rockskay.backend.otp.dto.OtpSendRequest;
import com.rockskay.backend.otp.dto.OtpVerifyRequest;

import java.util.UUID;

public interface OtpService {

    void sendOtp(OtpSendRequest otpSendRequest);

    void verifyOtp( OtpVerifyRequest otpVerifyRequest);
}