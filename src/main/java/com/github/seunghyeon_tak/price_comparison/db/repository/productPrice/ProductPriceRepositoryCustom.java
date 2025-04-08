package com.github.seunghyeon_tak.price_comparison.db.repository.productPrice;

import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.product.ProductPriceDto;

import java.util.List;

public interface ProductPriceRepositoryCustom {
    List<ProductPriceDto> findPriceDtoListByProductId(Long productId);
}
