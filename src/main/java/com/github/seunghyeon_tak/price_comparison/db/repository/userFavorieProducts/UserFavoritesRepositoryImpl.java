package com.github.seunghyeon_tak.price_comparison.db.repository.userFavorieProducts;

import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.user.UserFavoritesProductDto;
import com.github.seunghyeon_tak.price_comparison.db.domain.*;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class UserFavoritesRepositoryImpl implements UserFavoritesRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QUserFavoritesEntity uf = QUserFavoritesEntity.userFavoritesEntity;
    private final QProductEntity p = QProductEntity.productEntity;
    private final QProductImagesEntity i = QProductImagesEntity.productImagesEntity;
    private final QProductPriceEntity pp = QProductPriceEntity.productPriceEntity;
    private final QStoreEntity s = QStoreEntity.storeEntity;


    @Override
    public Page<UserFavoritesProductDto> findFavoritesByUserId(Long userId, Pageable pageable) {
        List<UserFavoritesProductDto> content = queryFactory
                .select(Projections.constructor(UserFavoritesProductDto.class,
                        p.id,
                        i.url,
                        s.name,
                        p.name,
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(pp.price)
                                        .from(pp)
                                        .where(pp.product.id.eq(p.id))
                                        .orderBy(pp.crawledAt.desc())
                                        .limit(1),
                                "price"
                        ),
                        uf.createdAt
                ))
                .from(uf)
                .join(uf.product, p)
                .join(p.store, s)
                .leftJoin(i).on(i.product.eq(p), i.isMain.isTrue())
                .where(uf.user.id.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(uf.createdAt.desc())
                .fetch();

        Long total = queryFactory
                .select(uf.count())
                .from(uf)
                .where(uf.user.id.eq(userId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }
}
