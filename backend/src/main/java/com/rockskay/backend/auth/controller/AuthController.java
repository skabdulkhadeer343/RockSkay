package com.rockskay.backend.auth.controller;

import com.rockskay.backend.auth.dto.*;
import com.rockskay.backend.auth.service.AuthService;
import com.rockskay.backend.common.constants.ApiEndpoints;
import com.rockskay.backend.common.dto.ApiResponse;
import com.rockskay.backend.common.dto.EmptyResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiEndpoints.AUTH)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.of(authService.register(request),
                        "Registration successful. Please verify your email."
                ));
    }

    @PostMapping("/login")
    public  ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request)
    {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(authService.login(request),
                        "Login successful."
                ));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(
            @Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.of(authService.refresh(request),
                        "Token Refresh Successful."
                ));
    }

    @PostMapping("/email-verification/send-otp")
    public ResponseEntity<EmptyResponse> sendVerificationOtp(
            @Valid @RequestBody EmailVerificationRequest request
    ) {
        authService.sendEmailVerificationOtp(request.email());

        return ResponseEntity.ok(
                EmptyResponse.of("OTP sent successfully."));
    }

    @PostMapping("/email-verification/verify-otp")
    public ResponseEntity<EmptyResponse> verifyEmail(
            @Valid @RequestBody EmailVerificationOtpVerifyRequest request
    )
    {
        authService.verifyEmail(request.email(), request.otp());

        return ResponseEntity.ok(
                EmptyResponse.of("Verified Successfully."));
    }


//    POST /api/v1/auth/password-reset/send
//    POST /api/v1/auth/password-reset/verify
//
//    POST /api/v1/resume/download/send
//    POST /api/v1/resume/download/verify
//
//    POST /api/v1/account/delete/send
//    POST /api/v1/account/delete/verify

}
