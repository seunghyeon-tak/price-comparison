package com.github.seunghyeon_tak.price_comparison.common.response.enums.user;

import com.github.seunghyeon_tak.price_comparison.common.response.enums.base.ResponseCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserResponseCode implements ResponseCodeIfs {
    USER_EMAIL_DUPLICATE(HttpStatus.BAD_REQUEST.value(), 1000, "중복된 이메일 입니다."),
    USER_NULL_POINT(HttpStatus.INTERNAL_SERVER_ERROR.value(), 1001, "user null point exception"),
    EMAIL_NOT_EXIST(HttpStatus.BAD_REQUEST.value(), 1002, "존재하지 않는 이메일입니다."),
    USER_PASSWORD_WRONG(HttpStatus.BAD_REQUEST.value(), 1003, "비밀번호가 틀렸습니다."),
    ;

    private final Integer httpStatusCode;
    private final Integer applicationStatusCode;
    private final String description;
}
