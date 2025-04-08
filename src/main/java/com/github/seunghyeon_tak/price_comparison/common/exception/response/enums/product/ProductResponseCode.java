package com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.product;

import com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.base.ResponseCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ProductResponseCode implements ResponseCodeIfs {
    PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), 3000, "찾는 상품이 없습니다."),
    PRODUCT_CATEGORY_NULL(HttpStatus.BAD_REQUEST.value(), 3001, "상품의 카테고리가 비어있습니다."),
    ;

    private final Integer httpStatusCode;
    private final Integer applicationStatusCode;
    private final String description;
}
