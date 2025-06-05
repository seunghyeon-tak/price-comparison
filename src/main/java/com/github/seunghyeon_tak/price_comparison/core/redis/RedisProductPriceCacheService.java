package com.github.seunghyeon_tak.price_comparison.core.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisProductPriceCacheService {
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisCacheProperties redisCacheProperties;
    private static final String PREFIX = "product:latest-price:";

    public void setLatestPrice(Long productId, BigDecimal price) {
        try {
            String key = PREFIX + productId;
            int ttlMinutes = redisCacheProperties.getProductPriceTtlMinutes();
            redisTemplate.opsForValue().set(key, price.toString(), ttlMinutes, TimeUnit.MINUTES);
            log.info("[Redis] [SET] 캐시 저장 완료 - key : {}, value : {}", key, price);
        } catch (Exception e) {
            log.warn("[Redis] [SET] 캐시 저장 실패 - productId : {}, error {}", productId, e.getMessage());
        }

    }

    public BigDecimal getLatestPrice(Long productId) {
        try {
            String key = PREFIX + productId;
            String value = redisTemplate.opsForValue().get(key);

            if (value != null) {
                log.info("[Redis] [GET] 캐시 조회 성공 - key : {}, value : {}", key, value);
                return new BigDecimal(value);
            } else {
                log.info("[Redis] [GET] 캐시 없음 - key : {}", key);
                return null;
            }
        } catch (Exception e) {
            log.warn("[Redis] [GET] 캐시 조회 실패 - productId : {}, error : {}", productId, e.getMessage());
            return null;
        }

    }

    public void deleteKey(Long productId) {
        try {
            String key = PREFIX + productId;
            redisTemplate.delete(key);
            log.info("[Redis] [DELETE] 캐시 삭제 완료 - key : {}", key);
        } catch (Exception e) {
            log.warn("[Redis] [DELETE] 캐시 삭제 실패 - productId {} : {}", productId, e.getMessage());
        }

    }
}
