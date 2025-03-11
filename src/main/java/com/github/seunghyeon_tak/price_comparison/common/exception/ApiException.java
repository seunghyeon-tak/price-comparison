package com.github.seunghyeon_tak.price_comparison.common.exception;

import com.github.seunghyeon_tak.price_comparison.common.response.enums.base.ResponseCodeIfs;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException implements ApiExceptionIfs {
    private final ResponseCodeIfs responseCodeIfs;
    private final String errorDescription;

    public ApiException(ResponseCodeIfs responseCodeIfs) {
        super(responseCodeIfs.getDescription());
        this.responseCodeIfs = responseCodeIfs;
        this.errorDescription = responseCodeIfs.getDescription();
    }

    public ApiException(ResponseCodeIfs responseCodeIfs, String errorDescription) {
        super(responseCodeIfs.getDescription());
        this.responseCodeIfs = responseCodeIfs;
        this.errorDescription = errorDescription;
    }

    @Override
    public ResponseCodeIfs getResponseCodeIfs() {
        return this.responseCodeIfs;
    }

    @Override
    public String getErrorDescription() {
        return this.errorDescription;
    }
}
