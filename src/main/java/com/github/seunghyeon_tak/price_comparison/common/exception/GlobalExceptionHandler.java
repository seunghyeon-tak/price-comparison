package com.github.seunghyeon_tak.price_comparison.common.exception;

import com.github.seunghyeon_tak.price_comparison.common.exception.response.Api;
import com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.base.ApiResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(2)
public class GlobalExceptionHandler {
    // 전역 예외처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Api<Void>> handleIllegalArgument(IllegalArgumentException exception) {
        log.error("HandleIllegalArgument Error >>> REQ_ID : {} | USER_ID : {} | message : {}",
                MDC.get("REQ_ID"), MDC.get("USER_ID"), exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Api.error(ApiResponseCode.INVALID_PARAMETER, exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Api<Void>> handlerException(Exception exception) {
        log.error("HandlerException Error >>> REQ_ID : {} | USER_ID : {} | message : {} ",
                MDC.get("REQ_ID"), MDC.get("USER_ID"), exception.getMessage(), exception);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Api.error(ApiResponseCode.INTERNAL_SERVER_ERROR, exception.getMessage()));
    }
}
