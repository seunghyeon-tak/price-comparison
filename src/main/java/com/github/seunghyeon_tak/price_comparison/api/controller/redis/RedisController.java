package com.github.seunghyeon_tak.price_comparison.api.controller.redis;

import com.github.seunghyeon_tak.price_comparison.core.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/redis")
public class RedisController {
    private final RedisService redisService;

    @PostMapping("/save")
    public String savePrice(@RequestParam String productId, @RequestParam String price) {
        redisService.savePrice(productId, new BigDecimal(price));
        return "가격 저장 완료";
    }

    @GetMapping("/get")
    public String getPrice(@RequestParam String productId) {
        String price = redisService.getPrice(productId);
        return price != null ? "현재 가격 : " + price : "가격 정보 없음";

    }
}
