package com.github.seunghyeon_tak.price_comparison.db.repository.productImage;

import com.github.seunghyeon_tak.price_comparison.db.domain.QProductImagesEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ProductImagesRepositoryImpl implements ProductImagesRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<String> findImageUrlByProductId(Long productId) {
        QProductImagesEntity productImages = QProductImagesEntity.productImagesEntity;

        return queryFactory
                .select(productImages.url)
                .from(productImages)
                .where(productImages.product.id.eq(productId))
                .orderBy(productImages.isMain.desc())
                .fetch();
    }
}
