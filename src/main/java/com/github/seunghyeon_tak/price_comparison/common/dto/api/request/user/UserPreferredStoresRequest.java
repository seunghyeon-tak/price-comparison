package com.github.seunghyeon_tak.price_comparison.common.dto.api.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPreferredStoresRequest {
    @NotEmpty(message = "storeIds는 비어있을 수 없습니다.")
    private List<Long> storeIds;
}
