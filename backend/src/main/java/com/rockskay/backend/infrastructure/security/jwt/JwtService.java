package com.rockskay.backend.infrastructure.security.jwt;

import com.rockskay.backend.infrastructure.config.AppProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final AppProperties appProperties;
    private final SecretKey secretKey;

    public String generateAccessToken(UserDetails userDetails) {

        Instant now = Instant.now();

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles",
                        userDetails.getAuthorities()
                                .stream()
                                .map(authority -> authority.getAuthority())
                                .toList())
                .issuer(appProperties.getJwt().getIssuer())
                .issuedAt(Date.from(now))
                .expiration(Date.from(
                        now.plus(Duration.ofMinutes(appProperties.getJwt().getAccessTokenExpiresInMins()))
                ))
                .signWith(secretKey)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractSubject(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {

        Claims claims = extractAllClaims(token);

        return claims.getSubject().equals(userDetails.getUsername())
                && claims.getExpiration()
                .toInstant()
                .isAfter(Instant.now());
    }

}