package com.github.seunghyeon_tak.price_comparison.common.exception;

import com.github.seunghyeon_tak.price_comparison.common.response.enums.base.ResponseCodeIfs;

public interface ApiExceptionIfs {
    ResponseCodeIfs getResponseCodeIfs();

    String getErrorDescription();
}
