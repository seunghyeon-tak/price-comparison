package com.github.seunghyeon_tak.price_comparison.core.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    // 가격 정보 저장
    public void savePrice(String productId, BigDecimal price) {
        redisTemplate.opsForValue().set("price : " + productId, price.toString(), Duration.ofMinutes(10));
    }

    // 가격 정보 조회
    public String getPrice(String productId) {
        return redisTemplate.opsForValue().get("price : " + productId);
    }

    // 정보 제거
    public void deleteKey(String productId) {
        redisTemplate.delete(productId);
    }
}
