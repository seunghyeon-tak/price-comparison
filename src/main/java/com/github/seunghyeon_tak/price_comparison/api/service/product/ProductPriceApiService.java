package com.github.seunghyeon_tak.price_comparison.api.service.product;

import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.product.ProductPriceDto;
import com.github.seunghyeon_tak.price_comparison.core.redis.RedisProductPriceCacheService;
import com.github.seunghyeon_tak.price_comparison.db.repository.productPrice.ProductPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductPriceApiService {
    private final ProductPriceRepository productPriceRepository;
    private final RedisProductPriceCacheService redisProductPriceCacheService;

    public List<ProductPriceDto> getProductPriceDto(Long productId) {
        return productPriceRepository.findPriceDtoListByProductId(productId);
    }

    public Map<Long, BigDecimal> findLatestPriceByProductIds(List<Long> productIds) {
        return productPriceRepository.findLatestPriceByProductIds(productIds);
    }

    public BigDecimal getCachePrice(Long productId) {
        // 단건 최신화 조회
        return redisProductPriceCacheService.getLatestPrice(productId);
    }

    public void cachePrice(Long productId, BigDecimal price) {
        redisProductPriceCacheService.setLatestPrice(productId, price);
    }
}
