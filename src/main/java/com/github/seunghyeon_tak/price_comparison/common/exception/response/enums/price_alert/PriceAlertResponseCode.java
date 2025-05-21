package com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.price_alert;

import com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.base.ResponseCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PriceAlertResponseCode implements ResponseCodeIfs {
    DUPLICATE_PRODUCT(HttpStatus.BAD_REQUEST.value(), 6000, "이미 같은 가격에 등록된 알림이 있습니다."),
    ;

    private final Integer httpStatusCode;
    private final Integer applicationStatusCode;
    private final String description;
}
