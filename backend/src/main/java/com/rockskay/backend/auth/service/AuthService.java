package com.rockskay.backend.auth.service;

import com.rockskay.backend.auth.dto.*;
import com.rockskay.backend.auth.entity.RefreshToken;
import com.rockskay.backend.common.exception.resource.DuplicateResourceException;
import com.rockskay.backend.common.exception.resource.ResourceNotVerifiedException;
import com.rockskay.backend.common.util.EmailUtil;
import com.rockskay.backend.security.config.JwtProperties;
import com.rockskay.backend.security.service.JwtService;
import com.rockskay.backend.user.dto.UserDto;
import com.rockskay.backend.user.entity.User;
import com.rockskay.backend.user.mapper.UserMapper;
import com.rockskay.backend.user.service.UserService;
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
    private final JwtProperties jwtProperties;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;



    @Transactional
    public RegisterResponse register(RegisterRequest request) {

        String email = EmailUtil.normalize(request.email());
        if (userService.existsByEmail(email)) {
            throw new DuplicateResourceException(
                    "User already exists with this email"
            );
        }

        User user = userService.createUser(request);
        return new RegisterResponse(user.getEmail());
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
                Duration.ofMinutes(jwtProperties.getRefreshTokenExpiresInMins())
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
                Duration.ofMinutes(jwtProperties.getAccessTokenExpiresInMins())
        );
        return AuthResponse.of(
                userDto,
                "Bearer",
                accessToken,
                newRefreshToken,
                expiresAt
        );
    }
}
