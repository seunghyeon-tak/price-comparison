package com.github.seunghyeon_tak.price_comparison.api.service.auth;

import com.github.seunghyeon_tak.price_comparison.api.service.user.UserApiService;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.auth.TokenRefreshResponse;
import com.github.seunghyeon_tak.price_comparison.common.exception.ApiException;
import com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.auth.AuthResponseCode;
import com.github.seunghyeon_tak.price_comparison.common.security.jwt.JwtProvider;
import com.github.seunghyeon_tak.price_comparison.db.domain.UserEntity;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthApiService {
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserApiService userApiService;

    public String refreshAccessToken(String refreshToken) {
        // 토큰 만료 여부 확인
        if (jwtProvider.isTokenExpired(refreshToken)) {
            throw new ApiException(AuthResponseCode.REFRESH_TOKEN_EXPIRATION);
        }

        // 토큰에서 userId 추출
        Claims claims = jwtProvider.getClaims(refreshToken);
        Long userId = Long.valueOf(claims.getSubject());

        // redis에 저장된 refreshToken과 비교
        if (!refreshTokenService.isValid(userId, refreshToken)) {
            throw new ApiException(AuthResponseCode.INVALID_REFRESH_TOKEN);
        }

        // 새 accessToken 발급
        UserEntity user = userApiService.getUserId(userId);

        return jwtProvider.generateAccessToken(user.getId(), user.getEmail());
    }
}
