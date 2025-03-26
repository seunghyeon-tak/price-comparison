package com.github.seunghyeon_tak.price_comparison.api.business.auth;

import com.github.seunghyeon_tak.price_comparison.api.converter.auth.AuthApiConverter;
import com.github.seunghyeon_tak.price_comparison.api.service.auth.AuthApiService;
import com.github.seunghyeon_tak.price_comparison.api.service.auth.dto.KakaoUserInfo;
import com.github.seunghyeon_tak.price_comparison.api.service.user.UserApiService;
import com.github.seunghyeon_tak.price_comparison.api.service.user.dto.LoginInfo;
import com.github.seunghyeon_tak.price_comparison.common.annotation.Business;
import com.github.seunghyeon_tak.price_comparison.common.annotation.BusinessLoggable;
import com.github.seunghyeon_tak.price_comparison.common.annotation.LogException;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.user.UserLoginRequest;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.auth.KakaoAccessTokenResponse;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.auth.TokenRefreshResponse;
import lombok.RequiredArgsConstructor;

@Business
@RequiredArgsConstructor
public class AuthApiBusiness {
    private final AuthApiService authApiService;
    private final AuthApiConverter authApiConverter;
    private final UserApiService userApiService;


    @BusinessLoggable("JWT 리프레쉬 토큰 재발급 비지니스 로직")
    @LogException
    public TokenRefreshResponse reissue(String refreshToken) {
        String accessToken = authApiService.refreshAccessToken(refreshToken);
        return authApiConverter.toResponse(accessToken);
    }

    @BusinessLoggable("로그인 비지니스")
    @LogException
    public LoginInfo login(UserLoginRequest request) {
        return authApiService.login(request);
    }

    @BusinessLoggable("카카오 로그인 비지니스 로직")
    @LogException
    public LoginInfo kakaoLogin(String code) {
        // 인가 코드로 카카오 토큰 요청
        KakaoAccessTokenResponse kakaoToken = authApiService.kakaoRequestAccessToken(code);

        // 카카오 사용자 정보 요청
        KakaoUserInfo kakaoUserInfo = authApiService.kakaoRequestUserInfo(kakaoToken.getAccessToken());
        String email = kakaoUserInfo.getKakaoAccount().getEmail();

        // 사용자 존재 여부 확인 + 자동 회원가입 처리
        return authApiService.loginOrSignupByKakao(email);
    }
}
