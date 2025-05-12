package com.github.seunghyeon_tak.price_comparison.fixture;

import com.github.seunghyeon_tak.price_comparison.db.domain.UserEntity;

public class UserFixture {
    public static UserEntity create(Long id, String email) {
        return UserEntity.builder()
                .id(id)
                .email(email)
                .password("test1234")
                .build();
    }
}
