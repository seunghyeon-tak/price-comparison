package com.github.seunghyeon_tak.price_comparison.common.response.enums.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ApiResponseCode implements ResponseCodeIfs {
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST.value(), 404, "잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), 500, "서버 오류가 발생했습니다.");

    private final Integer httpStatusCode;
    private final Integer applicationStatusCode;
    private final String description;
}
