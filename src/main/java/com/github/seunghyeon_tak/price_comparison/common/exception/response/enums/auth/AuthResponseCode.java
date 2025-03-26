package com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.auth;

import com.github.seunghyeon_tak.price_comparison.common.exception.ApiExceptionIfs;
import com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.base.ResponseCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthResponseCode implements ResponseCodeIfs {
    REFRESH_TOKEN_EXPIRATION(HttpStatus.UNAUTHORIZED.value(), 2000, "리프레쉬 토큰이 만료되었습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST.value(), 2001, "유효하지 않은 리프레쉬 토큰입니다."),
    ;

    private final Integer httpStatusCode;
    private final Integer applicationStatusCode;
    private final String description;
}
