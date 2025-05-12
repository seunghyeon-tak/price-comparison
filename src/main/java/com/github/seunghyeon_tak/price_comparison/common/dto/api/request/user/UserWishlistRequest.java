package com.github.seunghyeon_tak.price_comparison.common.dto.api.request.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserWishlistRequest {
    @NotNull(message = "상품 ID는 필수입니다.")
    private Long productId;
}
