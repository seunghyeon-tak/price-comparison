package com.github.seunghyeon_tak.price_comparison.common.exception;

import com.github.seunghyeon_tak.price_comparison.common.response.Api;
import com.github.seunghyeon_tak.price_comparison.common.response.enums.base.ResponseCodeIfs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(1)
public class ApiExceptionHandler {
    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<Api<Void>> handleApiException(ApiException apiException) {
        log.error("ApiException 발생 : {}", apiException.getMessage());
        ResponseCodeIfs responseCodeIfs = apiException.getResponseCodeIfs();
        return ResponseEntity
                .status(responseCodeIfs.getHttpStatusCode())
                .body(Api.error(responseCodeIfs, apiException.getErrorDescription()));
    }
}
