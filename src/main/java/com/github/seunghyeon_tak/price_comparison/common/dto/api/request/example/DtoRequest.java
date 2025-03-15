package com.github.seunghyeon_tak.price_comparison.common.dto.api.request.example;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DtoRequest {
    private String name;
    private int age;
}
