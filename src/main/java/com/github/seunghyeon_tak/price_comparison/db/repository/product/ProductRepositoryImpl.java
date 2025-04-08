package com.github.seunghyeon_tak.price_comparison.db.repository.product;

import com.github.seunghyeon_tak.price_comparison.db.domain.QCategoryEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.QProductEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public String findCategoryNameByProductId(Long productId) {
        QProductEntity product = QProductEntity.productEntity;
        QCategoryEntity category = QCategoryEntity.categoryEntity;

        return queryFactory
                .select(category.name)
                .from(product)
                .join(product.category, category)
                .where(product.id.eq(productId))
                .fetchOne();
    }
}
