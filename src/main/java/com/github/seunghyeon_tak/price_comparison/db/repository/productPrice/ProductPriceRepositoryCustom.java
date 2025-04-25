package com.github.seunghyeon_tak.price_comparison.db.repository.productPrice;

import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.product.ProductPriceDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ProductPriceRepositoryCustom {
    List<ProductPriceDto> findPriceDtoListByProductId(Long productId);

    Map<Long, BigDecimal> findLatestPriceByProductIds(List<Long> productIds);
}
