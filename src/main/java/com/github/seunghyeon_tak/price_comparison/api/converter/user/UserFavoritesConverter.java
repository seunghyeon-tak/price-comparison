package com.github.seunghyeon_tak.price_comparison.api.converter.user;

import com.github.seunghyeon_tak.price_comparison.common.annotation.Converter;
import com.github.seunghyeon_tak.price_comparison.db.domain.ProductEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.UserEntity;
import com.github.seunghyeon_tak.price_comparison.db.domain.UserFavoritesEntity;

@Converter
public class UserFavoritesConverter {
    public UserFavoritesEntity toEntity(UserEntity user, ProductEntity product) {
        return UserFavoritesEntity.builder()
                .user(user)
                .product(product)
                .build();
    }
}
