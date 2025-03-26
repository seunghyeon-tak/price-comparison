package com.github.seunghyeon_tak.price_comparison.api.controller.auth;

import com.github.seunghyeon_tak.price_comparison.api.business.auth.AuthApiBusiness;
import com.github.seunghyeon_tak.price_comparison.common.annotation.ControllerLoggable;
import com.github.seunghyeon_tak.price_comparison.common.dto.api.response.auth.TokenRefreshResponse;
import com.github.seunghyeon_tak.price_comparison.common.exception.response.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthApiController {
    private final AuthApiBusiness authApiBusiness;

    @PostMapping("/refresh")
    @ControllerLoggable("JWT 리프레쉬 토큰 재발급")
    public Api<TokenRefreshResponse> refresh(@CookieValue("refreshToken") String refreshToken) {
        TokenRefreshResponse response = authApiBusiness.reissue(refreshToken);
        return Api.success(response);
    }
}
