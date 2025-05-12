package com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.user;

import com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.base.ResponseCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserFavoritesResponseCode implements ResponseCodeIfs {
    EXISTENCE_IN_PRODUCT(HttpStatus.BAD_REQUEST.value(), 5000, "이미 찜한 상품입니다."),
    ;

    private final Integer httpStatusCode;
    private final Integer applicationStatusCode;
    private final String description;
}
