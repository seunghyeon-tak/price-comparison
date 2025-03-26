package com.github.seunghyeon_tak.price_comparison.common.dto.api.response.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TokenRefreshResponse {
    private String accessToken;
}
