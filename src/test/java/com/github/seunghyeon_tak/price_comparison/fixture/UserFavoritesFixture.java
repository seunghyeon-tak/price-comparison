package com.github.seunghyeon_tak.price_comparison.fixture;

import com.github.seunghyeon_tak.price_comparison.db.domain.ProductEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.UserEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.UserFavoritesEntity;

public class UserFavoritesFixture {
    public static UserFavoritesEntity create(UserEntity user, ProductEntity product) {
        return UserFavoritesEntity.builder()
                .user(user)
                .product(product)
                .build();
    }
}
