package com.github.seunghyeon_tak.price_comparison.common.util;

import org.springframework.http.ResponseCookie;

import java.time.Duration;

public class CookieUtils {
    private static final String REFRESH_TOKEN_COOKIE_NANE = "refreshToken";
    private static final long REFRESH_TOKEN_MAX_AGE = Duration.ofDays(7).getSeconds();

    public static ResponseCookie createRefreshTokenCookie(String token) {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NANE, token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(REFRESH_TOKEN_MAX_AGE)
                .sameSite("Lax")
                .build();
    }
}
