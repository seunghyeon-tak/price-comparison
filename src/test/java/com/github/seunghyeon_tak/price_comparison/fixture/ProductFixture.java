package com.github.seunghyeon_tak.price_comparison.fixture;

import com.github.seunghyeon_tak.price_comparison.db.domain.ProductEntity;

public class ProductFixture {
    public static ProductEntity create(Long id, String name) {
        return ProductEntity.builder()
                .id(id)
                .name(name)
                .build();
    }
}
