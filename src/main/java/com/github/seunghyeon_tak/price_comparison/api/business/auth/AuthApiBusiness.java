package com.github.seunghyeon_tak.price_comparison.api.business.auth;

import com.github.seunghyeon_tak.price_comparison.api.converter.auth.AuthApiConverter;
import com.github.seunghyeon_tak.price_comparison.api.service.auth.AuthApiService;
import com.github.seunghyeon_tak.price_comparison.common.annotation.Business;
import com.github.seunghyeon_tak.price_comparison.common.annotation.BusinessLoggable;
import com.github.seunghyeon_tak.price_comparison.common.annotation.LogException;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.auth.TokenRefreshResponse;
import lombok.RequiredArgsConstructor;

@Business
@RequiredArgsConstructor
public class AuthApiBusiness {
    private final AuthApiService authApiService;
    private final AuthApiConverter authApiConverter;


    @BusinessLoggable("JWT 리프레쉬 토큰 재발급 비지니스 로직")
    @LogException
    public TokenRefreshResponse reissue(String refreshToken) {
        String accessToken = authApiService.refreshAccessToken(refreshToken);
        return authApiConverter.toResponse(accessToken);
    }
}
