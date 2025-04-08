package com.github.seunghyeon_tak.price_comparison.db.repository.productPrice;

import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.product.ProductPriceDto;
import com.github.seunghyeon_tak.price_comparison.db.domain.QProductEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.QProductPriceEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.QStoreEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ProductPriceRepositoryImpl implements ProductPriceRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ProductPriceDto> findPriceDtoListByProductId(Long productId) {
        QProductPriceEntity productPrice = QProductPriceEntity.productPriceEntity;
        QProductEntity product = QProductEntity.productEntity;
        QStoreEntity store = QStoreEntity.storeEntity;

        return queryFactory
                .select(Projections.constructor(
                        ProductPriceDto.class,
                        store.name,
                        productPrice.price.intValue(),  // BigDecimal -> Integer 변환
                        product.purchaseUrl,
                        productPrice.crawledAt
                ))
                .from(productPrice)
                .join(productPrice.product, product)
                .join(productPrice.store, store)
                .where(product.id.eq(productId))
                .fetch();
    }
}
