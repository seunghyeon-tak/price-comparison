package com.github.seunghyeon_tak.price_comparison.common.exception;

import com.github.seunghyeon_tak.price_comparison.common.response.Api;
import com.github.seunghyeon_tak.price_comparison.common.response.enums.base.ResponseCodeIfs;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
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
        log.error("ApiException Error >>> REQ_ID : {} | USER_ID : {} | messsage : {} ",
                MDC.get("REQ_ID"), MDC.get("USER_ID"), apiException.getMessage(), apiException);
        ResponseCodeIfs responseCodeIfs = apiException.getResponseCodeIfs();
        return ResponseEntity
                .status(responseCodeIfs.getHttpStatusCode())
                .body(Api.error(responseCodeIfs, apiException.getErrorDescription()));
    }
}
