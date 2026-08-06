package com.rockskay.backend.auth.service;

import com.rockskay.backend.auth.entity.RefreshToken;
import com.rockskay.backend.auth.repository.RefreshTokenRepository;
import com.rockskay.backend.common.exception.auth.InvalidTokenException;
import com.rockskay.backend.common.exception.auth.TokenExpiredException;
import com.rockskay.backend.common.util.HashUtil;
import com.rockskay.backend.common.util.RandomUtil;
import com.rockskay.backend.security.config.JwtProperties;
import com.rockskay.backend.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;

    @Transactional
    public String createRefreshToken(User user) {

        String refreshTokenString = RandomUtil.generateSecureToken();



        RefreshToken refreshToken = RefreshToken.builder()
                .token(HashUtil.sha256(refreshTokenString))
                .expiresAt(calculateExpiry())
                .revoked(false)
                .build();

        user.addRefreshToken(refreshToken);

        refreshTokenRepository.save(refreshToken);

        return refreshTokenString;
    }

    public RefreshToken validateRefreshToken(String refreshToken)
    {
        String refreshTokenHash = HashUtil.sha256(refreshToken);
        RefreshToken token = refreshTokenRepository
                .findByToken(refreshTokenHash)
                .orElseThrow(() ->
                        new InvalidTokenException("Invalid refresh token.")
                );

        if (token.isRevoked()) {
            throw new InvalidTokenException("Refresh token has been revoked.");
        }

        if (token.getExpiresAt().isBefore(Instant.now())) {
            token.setRevoked(true);
            throw new TokenExpiredException("Refresh token has expired.");
        }

        return token;
    }

    @Transactional
    public void revoke(RefreshToken token) {
        token.setRevoked(true);
    }


    @Transactional
    public void revokeAll(User user)
    {
        user.getRefreshTokens()
                .forEach(token -> token.setRevoked(true));
    }

    private Instant calculateExpiry() {
        return Instant.now().plus(
                Duration.ofMinutes(jwtProperties.getRefreshTokenExpiresInMins())
        );
    }
}

