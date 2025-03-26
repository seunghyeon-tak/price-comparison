package com.github.seunghyeon_tak.price_comparison.common.dto.api.response.user;

import com.github.seunghyeon_tak.price_comparison.db.enums.AlertType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UserInfoDto {
    private Long id;
    private String email;
    private String nickname;
    private AlertType alertType;
}
