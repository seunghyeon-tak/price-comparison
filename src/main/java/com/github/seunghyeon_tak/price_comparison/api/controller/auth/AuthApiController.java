package com.github.seunghyeon_tak.price_comparison.api.controller.auth;

import com.github.seunghyeon_tak.price_comparison.api.business.auth.AuthApiBusiness;
import com.github.seunghyeon_tak.price_comparison.api.service.user.dto.LoginInfo;
import com.github.seunghyeon_tak.price_comparison.common.annotation.ControllerLoggable;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.request.user.UserLoginRequest;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.auth.TokenRefreshResponse;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.user.UserInfoDto;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.user.UserLoginResponse;
import com.github.seunghyeon_tak.price_comparison.common.exception.response.Api;
import com.github.seunghyeon_tak.price_comparison.common.util.CookieUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth API", description = "인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthApiController {
    private final AuthApiBusiness authApiBusiness;

    @PostMapping("/refresh")
    @ControllerLoggable("JWT 리프레쉬 토큰 재발급 컨트롤러 로직")
    public Api<TokenRefreshResponse> refresh(@CookieValue("refreshToken") String refreshToken) {
        TokenRefreshResponse response = authApiBusiness.reissue(refreshToken);
        return Api.success(response);
    }

    @PostMapping("/login")
    @ControllerLoggable("로그인 컨트롤러")
    public Api<UserLoginResponse> login(@RequestBody @Valid UserLoginRequest request, HttpServletResponse response) {
        LoginInfo loginInfo = authApiBusiness.login(request);

        // HttpOnly Cookie
        ResponseCookie refreshCookie = CookieUtils.createRefreshTokenCookie(loginInfo.getRefreshToken());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return Api.success(UserLoginResponse.builder()
                .accessToken(loginInfo.getAccessToken())
                .user(UserInfoDto.builder()
                        .id(loginInfo.getUserId())
                        .email(loginInfo.getEmail())
                        .nickname(loginInfo.getNickname())
                        .alertType(loginInfo.getAlertType())
                        .build())
                .build());
    }

    @GetMapping("/kakao-login")
    @ControllerLoggable("카카오 로그인 컨트롤러 로직")
    public Api<UserLoginResponse> kakaoLogin(@RequestParam String code, HttpServletResponse response) {
        LoginInfo loginInfo = authApiBusiness.kakaoLogin(code);

        // refreshToken - HttpOnly Cookie save
        ResponseCookie refreshCookie = CookieUtils.createRefreshTokenCookie(loginInfo.getRefreshToken());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return Api.success(UserLoginResponse.builder()
                .accessToken(loginInfo.getAccessToken())
                .user(UserInfoDto.builder()
                        .id(loginInfo.getUserId())
                        .email(loginInfo.getEmail())
                        .nickname(loginInfo.getNickname())
                        .alertType(loginInfo.getAlertType())
                        .build())
                .build());
    }
}
