package com.sg.ecomarket.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    SUCCESS(200, "操作成功"),
    SYSTEM_ERROR(500, "系统错误"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),

    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ALREADY_EXISTS(1002, "用户已存在"),
    USER_PASSWORD_ERROR(1003, "密码错误"),

    TOKEN_INVALID(2001, "Token无效"),
    TOKEN_EXPIRED(2002, "Token已过期");

    private final Integer code;
    private final String message;
}
