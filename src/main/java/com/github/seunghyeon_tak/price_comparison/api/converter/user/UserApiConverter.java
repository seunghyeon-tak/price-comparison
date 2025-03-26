package com.github.seunghyeon_tak.price_comparison.api.converter.user;

import com.github.seunghyeon_tak.price_comparison.common.annotation.Converter;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.user.UserSignupRequest;
import com.github.seunghyeon_tak.price_comparison.common.exception.ApiException;
import com.github.seunghyeon_tak.price_comparison.common.response.enums.user.UserResponseCode;
import com.github.seunghyeon_tak.price_comparison.db.domain.UserEntity;

@Converter
public class UserApiConverter {
    public UserEntity toEntity(UserSignupRequest request) {
        if (request == null) {
            throw new ApiException(UserResponseCode.USER_NULL_POINT);
        }

        return UserEntity.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }

}
