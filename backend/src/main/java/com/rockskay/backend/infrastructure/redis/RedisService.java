package com.rockskay.backend.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    private static final DefaultRedisScript<Long> INCREMENT_WITH_EXPIRY =
            new DefaultRedisScript<>(
                    """
                    local count = redis.call('INCR', KEYS[1])

                    if count == 1 then
                        redis.call('EXPIRE', KEYS[1], ARGV[1])
                    end

                    return count
                    """,
                    Long.class
            );

    private static final DefaultRedisScript<Long> INCREMENT_ATTEMPTS =
            new DefaultRedisScript<>(
                    """
                    local attempts = redis.call('INCR', KEYS[1])

                    if attempts == 1 then
                        redis.call('EXPIRE', KEYS[1], ARGV[1])
                    end

                    if attempts >= tonumber(ARGV[2]) then
                        redis.call('DEL', ARGV[3])
                    end

                    return attempts
                    """,
                    Long.class
            );

    public void set(
            String key,
            String value,
            Duration ttl
    ) {
        redisTemplate.opsForValue()
                .set(key, value, ttl);
    }

    public boolean setIfAbsent(
            String key,
            String value,
            Duration ttl
    ) {
        return Boolean.TRUE.equals(
                redisTemplate.opsForValue()
                        .setIfAbsent(key, value, ttl)
        );
    }

    public String get(String key) {
        return redisTemplate.opsForValue()
                .get(key);
    }

    public Long increment(String key) {
        return redisTemplate.opsForValue()
                .increment(key);
    }

    public Long incrementWithExpiry(
            String key,
            Duration window
    ) {
        return redisTemplate.execute(
                INCREMENT_WITH_EXPIRY,
                List.of(key),
                String.valueOf(window.getSeconds())
        );
    }

    public Long incrementAttempts(
            String attemptsKey,
            String otpKey,
            Duration ttl,
            int maxAttempts
    ) {
        return redisTemplate.execute(
                INCREMENT_ATTEMPTS,
                List.of(attemptsKey),
                String.valueOf(ttl.getSeconds()),
                String.valueOf(maxAttempts),
                otpKey
        );
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public boolean exists(String key) {
        return Boolean.TRUE.equals(
                redisTemplate.hasKey(key)
        );
    }

    public Long getTtl(String key) {
        return redisTemplate.getExpire(key);
    }
}