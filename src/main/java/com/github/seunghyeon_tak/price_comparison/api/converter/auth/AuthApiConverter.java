package com.github.seunghyeon_tak.price_comparison.api.converter.auth;

import com.github.seunghyeon_tak.price_comparison.common.annotation.Converter;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.auth.TokenRefreshResponse;

@Converter
public class AuthApiConverter {
    public TokenRefreshResponse toResponse(String accessToken) {
        return TokenRefreshResponse.builder()
                .accessToken(accessToken)
                .build();
    }
}
