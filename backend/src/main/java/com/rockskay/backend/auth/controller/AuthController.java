package com.rockskay.backend.auth.controller;

import com.rockskay.backend.auth.dto.*;
import com.rockskay.backend.auth.service.AuthService;
import com.rockskay.backend.common.constant.ApiEndpoints;
import com.rockskay.backend.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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


}
