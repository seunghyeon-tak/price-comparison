package com.github.seunghyeon_tak.price_comparison.core.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisProductPriceCacheService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String PREFIX = "product:latest-price:";

    public void setLatestPrice(Long productId, BigDecimal price) {
        String key = PREFIX + productId;
        redisTemplate.opsForValue().set(key, price.toString(), 10, TimeUnit.MINUTES);
    }

    public BigDecimal getLatestPrice(Long productId) {
        String key = PREFIX + productId;
        String value = redisTemplate.opsForValue().get(key);

        return value != null ? new BigDecimal(value) : null;
    }

    public void deleteKey(Long productId) {
        redisTemplate.delete(PREFIX + productId);
    }
}
