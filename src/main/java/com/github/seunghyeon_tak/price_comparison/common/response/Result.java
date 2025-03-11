package com.github.seunghyeon_tak.price_comparison.common.response;

import com.github.seunghyeon_tak.price_comparison.common.response.enums.base.ResponseCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Result {
    private int resultCode;
    private String resultMessage;
    private String resultDescription;

    public static Result OK() {
        return Result.builder()
                .resultCode(200)
                .resultMessage("success")
                .resultDescription("success")
                .build();
    }

    public static Result ERROR(ResponseCodeIfs responseCodeIfs) {
        return Result.builder()
                .resultCode(responseCodeIfs.getApplicationStatusCode())
                .resultMessage(responseCodeIfs.getDescription())
                .resultDescription("fail")
                .build();
    }

    public static Result ERROR(ResponseCodeIfs responseCodeIfs, String resultDescription) {
        return Result.builder()
                .resultCode(responseCodeIfs.getApplicationStatusCode())
                .resultMessage(responseCodeIfs.getDescription())
                .resultDescription(resultDescription)
                .build();
    }
}
