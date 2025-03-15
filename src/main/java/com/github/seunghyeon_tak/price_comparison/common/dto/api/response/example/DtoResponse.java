package com.github.seunghyeon_tak.price_comparison.common.dto.api.response.example;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DtoResponse {
    private String name;
    private int age;
}
