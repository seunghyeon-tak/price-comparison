package com.github.seunghyeon_tak.price_comparison.fixture;

import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.user.UserFavoritesProductDto;
import com.github.seunghyeon_tak.price_comparison.db.domain.ProductEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.UserEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.UserFavoritesEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class UserFavoritesFixture {
    public static UserFavoritesEntity create(UserEntity user, ProductEntity product) {
        return UserFavoritesEntity.builder()
                .user(user)
                .product(product)
                .build();
    }

    public static UserFavoritesProductDto createFavoritesDtp() {
        return UserFavoritesProductDto.builder()
                .id(1L)
                .name("테스트 상품")
                .storeName("테스트 스토어")
                .imageUrl("https://image.com/sample.jpg")
                .price(new BigDecimal("10000"))
                .favoritedAt(LocalDateTime.now())
                .build();
    }
}
