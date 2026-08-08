package com.rockskay.backend.infrastructure.config;

import com.rockskay.backend.otp.constant.OtpPurpose;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.EnumMap;
import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private Jwt jwt = new Jwt();
    private Otp otp = new Otp();
    private Mail mail = new Mail();


    @Getter
    @Setter
    public static class Jwt {

        private String secret;
        private long accessTokenExpiresInMins;
        private long refreshTokenExpiresInMins;
        private String issuer;
    }

    @Getter
    @Setter
    public static class Otp {

        private int length = 6;

        private Map<OtpPurpose, OtpPolicy> policies =
                new EnumMap<>(OtpPurpose.class);
    }

    @Getter
    @Setter
    public static class OtpPolicy {

        private Duration ttl;
        private Duration resendCooldown;
        private int maxAttempts;

        private int maxRequests;
        private Duration requestWindow;
    }

    @Getter
    @Setter
    public static class Mail {

        private String from;
    }
}