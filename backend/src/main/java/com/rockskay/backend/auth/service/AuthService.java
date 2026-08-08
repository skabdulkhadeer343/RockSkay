package com.rockskay.backend.auth.service;

import com.rockskay.backend.auth.dto.*;
import com.rockskay.backend.auth.entity.RefreshToken;
import com.rockskay.backend.common.exception.resource.DuplicateResourceException;
import com.rockskay.backend.common.exception.resource.ResourceAlreadyVerifiedException;
import com.rockskay.backend.common.exception.resource.ResourceNotVerifiedException;
import com.rockskay.backend.common.util.EmailUtil;
import com.rockskay.backend.infrastructure.config.AppProperties;
import com.rockskay.backend.infrastructure.security.jwt.JwtService;
import com.rockskay.backend.otp.constant.OtpChannel;
import com.rockskay.backend.otp.constant.OtpPurpose;
import com.rockskay.backend.otp.dto.OtpSendRequest;
import com.rockskay.backend.otp.dto.OtpVerifyRequest;
import com.rockskay.backend.otp.service.OtpService;
import com.rockskay.backend.user.dto.UserDto;
import com.rockskay.backend.user.entity.User;
import com.rockskay.backend.user.mapper.UserMapper;
import com.rockskay.backend.user.service.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final AppProperties appProperties;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final OtpService otpService;



    @Transactional
    public RegisterResponse register(RegisterRequest request) {

        String email = EmailUtil.normalize(request.email());
        if (userService.existsByEmail(email)) {
            throw new DuplicateResourceException(
                    "User already exists with this email"
            );
        }

        User user = userService.createUser(request);

        return new RegisterResponse(user.getId(), user.getEmail());
    }

    public AuthResponse login(LoginRequest request) {

        String email = EmailUtil.normalize(request.email());
        User user = userService.findByEmail(email);

        if (!user.isVerified()) {
            throw new ResourceNotVerifiedException(
                    "Please verify your email before logging in."
            );
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        request.password()
                )
        );

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(email);

        String accessToken =
                jwtService.generateAccessToken(userDetails);

        String refreshToken =
                refreshTokenService.createRefreshToken(user);

        UserDto userDto = userMapper.toDto(user);

        Instant now = Instant.now();

        Instant expiresAt = now.plus(
                Duration.ofMinutes(appProperties.getJwt().getAccessTokenExpiresInMins())
        );
        return AuthResponse.of( userDto,
                "Bearer",
                accessToken,
                refreshToken,
                expiresAt
        );
    }

    public AuthResponse refresh(RefreshTokenRequest refreshTokenrequest) {

        RefreshToken token =
                refreshTokenService.validateRefreshToken(refreshTokenrequest.refreshToken());

        User user = token.getUser();

        refreshTokenService.revoke(token);

        UserDetails userDetails =
                userDetailsService.loadUserByUsername(user.getEmail());

        String accessToken =
                jwtService.generateAccessToken(userDetails);

        String newRefreshToken =
                refreshTokenService.createRefreshToken(user);

        UserDto userDto = userMapper.toDto(user);

        Instant now = Instant.now();

        Instant expiresAt = now.plus(
                Duration.ofMinutes(appProperties.getJwt().getAccessTokenExpiresInMins())
        );
        return AuthResponse.of(
                userDto,
                "Bearer",
                accessToken,
                newRefreshToken,
                expiresAt
        );
    }

    public void sendEmailVerificationOtp(String email) {

        String normalizedEmail = EmailUtil.normalize(email);

        User user = userService.findByEmail(normalizedEmail);

        if (user.isVerified()) {
            throw new ResourceAlreadyVerifiedException(
                    "Email is already verified."
            );
        }
        OtpSendRequest otpSendRequest =
                new OtpSendRequest(email,
                        email,
                        OtpPurpose.EMAIL_VERIFICATION,
                        OtpChannel.EMAIL);

        otpService.sendOtp(otpSendRequest);

    }

    public void verifyEmail(String email, String otp) {

        String normalizedEmail = EmailUtil.normalize(email);

        User user = userService.findByEmail(normalizedEmail);

        if (user.isVerified()) {
            throw new ResourceAlreadyVerifiedException(
                    "Email is already verified."
            );
        }

        OtpVerifyRequest otpVerifyRequest =
                new OtpVerifyRequest(email,
                        OtpPurpose.EMAIL_VERIFICATION,
                        OtpChannel.EMAIL,
                        otp);

        otpService.verifyOtp(otpVerifyRequest);

        userService.verifyUser(normalizedEmail);
    }
}
