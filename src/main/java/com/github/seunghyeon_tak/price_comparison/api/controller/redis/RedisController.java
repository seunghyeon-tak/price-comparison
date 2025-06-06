package com.github.seunghyeon_tak.price_comparison.api.controller.redis;

import com.github.seunghyeon_tak.price_comparison.core.redis.RedisProductPriceCacheService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(name = "Redis API", description = "가격 최신화 관련 Redis API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/redis")
public class RedisController {
    private final RedisProductPriceCacheService redisService;

    @PostMapping("/save")
    public String savePrice(@RequestParam String productId, @RequestParam String price) {
        redisService.setLatestPrice(Long.valueOf(productId), new BigDecimal(price));
        return "가격 저장 완료";
    }

    @GetMapping("/get")
    public String getPrice(@RequestParam String productId) {
        BigDecimal price = redisService.getLatestPrice(Long.valueOf(productId));
        return price != null ? "현재 가격 : " + price : "가격 정보 없음";

    }
}
