package com.rockskay.backend.common.util;

import java.security.SecureRandom;
import java.util.Base64;

public final class RandomUtil {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private RandomUtil() {
    }

    public static String generateSecureToken() {
        byte[] bytes = new byte[64]; // 512-bit token
        SECURE_RANDOM.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }
}