package com.github.seunghyeon_tak.price_comparison.api.service.user.dto;

import com.github.seunghyeon_tak.price_comparison.db.enums.AlertType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LoginInfo {
    private final String accessToken;
    private final String refreshToken;
    private final Long userId;
    private final String email;
    private final String nickname;
    private final AlertType alertType;
}
