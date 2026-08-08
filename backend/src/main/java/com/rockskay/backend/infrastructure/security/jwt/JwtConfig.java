package com.rockskay.backend.infrastructure.security.jwt;

import com.rockskay.backend.infrastructure.config.AppProperties;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Configuration
public class JwtConfig {

    @Bean
    public SecretKey jwtSecretKey(AppProperties appProperties) {
        return Keys.hmacShaKeyFor(
                appProperties.getJwt().getSecret()
                        .getBytes(StandardCharsets.UTF_8)
        );
    }
}