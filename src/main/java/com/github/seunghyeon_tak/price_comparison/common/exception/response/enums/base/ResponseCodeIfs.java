package com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.base;

public interface ResponseCodeIfs {
    Integer getHttpStatusCode();

    Integer getApplicationStatusCode();

    String getDescription();
}
