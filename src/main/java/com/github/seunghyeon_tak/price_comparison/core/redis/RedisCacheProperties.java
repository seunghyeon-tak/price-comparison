package com.github.seunghyeon_tak.price_comparison.core.redis;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "redis.cache")
public class RedisCacheProperties {
    private int productPriceTtlMinutes = 10;

    public void setProductPriceTtlMinutes(int minutes) {
        this.productPriceTtlMinutes = minutes;
    }
}
