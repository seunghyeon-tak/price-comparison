package com.github.seunghyeon_tak.price_comparison.common.dto.api.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserLoginResponse {
    private final String accessToken;
    private final String refreshToken;
    private final UserInfoDto user;
}
