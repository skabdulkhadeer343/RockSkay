package com.rockskay.backend.otp.service;

import com.rockskay.backend.common.util.HashUtil;
import com.rockskay.backend.common.util.RandomUtil;
import com.rockskay.backend.infrastructure.config.AppProperties;
import com.rockskay.backend.infrastructure.redis.RedisKeys;
import com.rockskay.backend.infrastructure.redis.RedisService;
import com.rockskay.backend.otp.constant.OtpChannel;
import com.rockskay.backend.otp.constant.OtpPurpose;
import com.rockskay.backend.otp.dto.OtpSendRequest;
import com.rockskay.backend.otp.dto.OtpVerifyRequest;
import com.rockskay.backend.otp.exception.InvalidOtpException;
import com.rockskay.backend.otp.exception.OtpAttemptsExceededException;
import com.rockskay.backend.otp.exception.OtpCooldownException;
import com.rockskay.backend.otp.exception.OtpNotFoundException;
import com.rockskay.backend.otp.exception.OtpRateLimitExceededException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final AppProperties appProperties;
    private final RedisService redisService;
    private final OtpDeliveryService otpDeliveryService;


    @Override
    public void sendOtp(OtpSendRequest otpSendRequest) {

        String userId = otpSendRequest.userId();
        String destination = otpSendRequest.destination();;
        OtpPurpose purpose = otpSendRequest.purpose();
        OtpChannel channel = otpSendRequest.channel();
        AppProperties.OtpPolicy policy =
                getPolicy(purpose);

        String otpKey =
                RedisKeys.otp(userId, purpose, channel);

        String attemptsKey =
                RedisKeys.otpAttempts(userId, purpose, channel);

        String cooldownKey =
                RedisKeys.otpResendCooldown(userId, purpose, channel);

        String rateLimitKey =
                RedisKeys.otpRateLimit(userId, purpose, channel);

        // 1. Resend cooldown
        if (redisService.exists(cooldownKey)) {
            throw new OtpCooldownException();
        }

        // 2. Request-window rate limit
        Long requestCount =
                redisService.incrementWithExpiry(
                        rateLimitKey,
                        policy.getRequestWindow()
                );

        if (requestCount > policy.getMaxRequests()) {

            // Don't leave a request that exceeded the limit
            // counted beyond the configured window.
            throw new OtpRateLimitExceededException();
        }

        // 3. Generate OTP
        String otp = RandomUtil.generateNumberToken(
                appProperties.getOtp().getLength());

        // 4. Hash OTP
        String otpHash = HashUtil.sha256(otp);

        // 5. Store OTP
        redisService.set(
                otpKey,
                otpHash,
                policy.getTtl()
        );

        // 6. Reset attempts
        redisService.set(
                attemptsKey,
                "0",
                policy.getTtl()
        );

        // 7. Set resend cooldown
        redisService.set(
                cooldownKey,
                "1",
                policy.getResendCooldown()
        );

        // 8. Send OTP
        otpDeliveryService.send(
                channel,
                destination,
                otp,
                purpose,
                policy.getTtl()
        );

    }

    @Override
    public void verifyOtp(OtpVerifyRequest otpVerifyRequest) {

        String userId = otpVerifyRequest.userId();
        OtpPurpose purpose = otpVerifyRequest.purpose();
        OtpChannel channel = otpVerifyRequest.channel();
        String otp = otpVerifyRequest.otp();
        AppProperties.OtpPolicy policy =
                getPolicy(purpose);

        String otpKey =
                RedisKeys.otp(userId, purpose, channel);

        String attemptsKey =
                RedisKeys.otpAttempts(userId, purpose, channel);

        String storedHash =
                redisService.get(otpKey);

        String otpHash = HashUtil.sha256(otp);

        // OTP does not exist or expired.
        if (storedHash == null) {
            throw new OtpNotFoundException();
        }

        String attemptsValue =
                redisService.get(attemptsKey);

        int attempts =
                attemptsValue == null
                        ? 0
                        : Integer.parseInt(attemptsValue);

        if (attempts >= policy.getMaxAttempts()) {
            redisService.delete(otpKey);
            redisService.delete(attemptsKey);

            throw new OtpAttemptsExceededException();
        }

        // Check OTP.
        boolean matches = otpHash.equals(storedHash);

        if (!matches) {

            Long newAttempts =
                    redisService.incrementAttempts(
                            attemptsKey,
                            otpKey,
                            policy.getTtl(),
                            policy.getMaxAttempts()
                    );

            if (newAttempts >= policy.getMaxAttempts()) {
                redisService.delete(attemptsKey);
                throw new OtpAttemptsExceededException();
            }

            throw new InvalidOtpException("Invalid Otp");
        }

        // Successful verification.
        redisService.delete(otpKey);
        redisService.delete(attemptsKey);
    }




    private AppProperties.OtpPolicy getPolicy(
            OtpPurpose purpose
    ) {
        AppProperties.OtpPolicy policy =
                appProperties.getOtp()
                        .getPolicies()
                        .get(purpose);

        if (policy == null) {
            throw new IllegalStateException(
                    "OTP policy not configured for purpose: "
                            + purpose
            );
        }

        return policy;
    }
}