package com.github.seunghyeon_tak.price_comparison.common.dto.api.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class UserFavoritesProductDto {
    private Long id;
    private String imageUrl;
    private String storeName;
    private String name;
    private BigDecimal price;
    private LocalDateTime favoritedAt;
}
