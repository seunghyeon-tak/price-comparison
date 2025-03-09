package com.github.seunghyeon_tak.price_comparison.db.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AlertType {
    NONE("끄기"),
    EMAIL("이메일"),
    SMS("문자"),
    PUSH("앱 푸시");

    private final String description;
}
