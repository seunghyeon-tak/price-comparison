package com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.store;

import com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.base.ResponseCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum StoreResponseCode implements ResponseCodeIfs {
    STORE_IS_EMPTY(HttpStatus.BAD_REQUEST.value(), 4000, "존재하지 않는 storeId 입니다."),
    ;

    private final Integer httpStatusCode;
    private final Integer applicationStatusCode;
    private final String description;
}
