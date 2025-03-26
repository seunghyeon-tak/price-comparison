package com.github.seunghyeon_tak.price_comparison.common.exception.response;

import com.github.seunghyeon_tak.price_comparison.common.exception.response.enums.base.ResponseCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Api<T> {
    private Result result;
    private T data;

    public static Api<Void> success() {
        Api<Void> api = new Api<>();
        api.result = Result.OK();

        return api;
    }

    public static <T> Api<T> success(T data) {
        Api<T> api = new Api<>();
        api.result = Result.OK();
        api.data = data;

        return api;
    }

    public static <T> Api<T> error(ResponseCodeIfs responseCodeIfs) {
        Api<T> api = new Api<>();
        api.result = Result.ERROR(responseCodeIfs);

        return api;
    }

    public static <T> Api<T> error(ResponseCodeIfs responseCodeIfs, String description) {
        Api<T> api = new Api<>();
        api.result = Result.ERROR(responseCodeIfs, description);

        return api;
    }
}
