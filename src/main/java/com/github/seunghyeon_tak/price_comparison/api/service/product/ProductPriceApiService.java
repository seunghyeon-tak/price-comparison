package com.github.seunghyeon_tak.price_comparison.api.service.product;

import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.product.ProductPriceDto;
import com.github.seunghyeon_tak.price_comparison.db.repository.productPrice.ProductPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductPriceApiService {
    private final ProductPriceRepository productPriceRepository;

    public List<ProductPriceDto> getProductPriceDto(Long productId) {
        return productPriceRepository.findPriceDtoListByProductId(productId);
    }

    public Map<Long, BigDecimal> findLatestPriceByProductIds(List<Long> productIds) {
        return productPriceRepository.findLatestPriceByProductIds(productIds);
    }
}
