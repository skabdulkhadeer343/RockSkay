package com.rockskay.backend.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.jwt")
@Getter
@Setter
public class JwtProperties {

    private String key;
    private long accessTokenExpiresInMins;
    private long refreshTokenExpiresInMins;
    private String issuer;
}
