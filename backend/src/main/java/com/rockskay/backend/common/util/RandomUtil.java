package com.rockskay.backend.common.util;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

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

    public static String generateNumberToken(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than 0");
        }

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(SECURE_RANDOM.nextInt(10));
        }
        return sb.toString();

    }
}