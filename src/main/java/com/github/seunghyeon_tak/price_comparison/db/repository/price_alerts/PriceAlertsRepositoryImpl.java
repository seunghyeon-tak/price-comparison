package com.github.seunghyeon_tak.price_comparison.db.repository.price_alerts;

import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.price_alerts.PriceAlertsDto;
import com.github.seunghyeon_tak.price_comparison.db.domain.QPriceAlertsEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.QProductEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.QProductImagesEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.QProductPriceEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class PriceAlertsRepositoryImpl implements PriceAlertsRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PriceAlertsDto> findPriceAlertsWithLatestPrice(Long userId, Pageable pageable) {
        QPriceAlertsEntity alert = QPriceAlertsEntity.priceAlertsEntity;
        QProductEntity product = QProductEntity.productEntity;
        QProductImagesEntity image = QProductImagesEntity.productImagesEntity;
        QProductPriceEntity price = QProductPriceEntity.productPriceEntity;

        List<PriceAlertsDto> content = queryFactory
                .select(Projections.constructor(
                        PriceAlertsDto.class,
                        alert.id,
                        product.id,
                        product.name,
                        image.url,
                        price.price.max(),
                        alert.targetPrice,
                        alert.isActive,
                        alert.createdAt
                ))
                .from(alert)
                .join(alert.product, product)
                .leftJoin(image).on(image.product.eq(product), image.isMain.isTrue())
                .leftJoin(price).on(price.product.eq(product))
                .where(alert.user.id.eq(userId).and(alert.isActive.isTrue()))
                .groupBy(alert.id, product.id, product.name, image.url, alert.targetPrice, alert.isActive, alert.createdAt)
                .fetch();

        Long total = queryFactory
                .select(alert.count())
                .from(alert)
                .where(alert.user.id.eq(userId).and(alert.isActive.isTrue()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
