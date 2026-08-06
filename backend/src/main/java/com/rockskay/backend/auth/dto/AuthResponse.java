package com.rockskay.backend.auth.dto;

import com.rockskay.backend.user.dto.UserDto;

import java.time.Instant;

public record AuthResponse(
        UserDto user,
        String tokenType,
        String accessToken,
        String refreshToken,
        Instant expiresAt
) {

    public static AuthResponse of(UserDto user,
                                  String tokenType,
                                  String accessToken,
                                  String refreshToken,
                                  Instant expiresIn)
    {
        return new AuthResponse(user, tokenType, accessToken, refreshToken, expiresIn);
    }
}
