package com.github.seunghyeon_tak.price_comparison.api.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String REFRESH_PREFIX = "refresh:";

    public void save(Long userId, String refreshToken, long expiryMillis) {
        redisTemplate.opsForValue().set(
                REFRESH_PREFIX + userId,
                refreshToken,
                expiryMillis,
                TimeUnit.MILLISECONDS
        );
    }

    public boolean isValid(Long userId, String refreshToken) {
        String stored = redisTemplate.opsForValue().get(REFRESH_PREFIX + userId);
        return stored != null && stored.equals(refreshToken);
    }
}
