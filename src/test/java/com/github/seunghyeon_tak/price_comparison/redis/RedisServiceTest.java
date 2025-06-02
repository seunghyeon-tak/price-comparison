package com.github.seunghyeon_tak.price_comparison.redis;

import com.github.seunghyeon_tak.price_comparison.core.redis.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class RedisServiceTest {
    @Autowired
    RedisService redisService;

    @Test
    void test1() {
        String productId = "price:key";
        int price = 15000;

        redisService.savePrice(productId, BigDecimal.valueOf(price));
        String result = redisService.getPrice(productId);

        assertThat(result).isEqualTo(price);

        redisService.deleteKey(productId);
    }
}
