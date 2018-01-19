package com.geekzhang.demo.enums;

public enum ResponseCode {
    KEY("code","msg"),
    SUCCESS("0000", "成功"),
    REFUSE("403","拒绝访问"),
    NO_USER("U0001", "用户名不存在"),
    PASS_WRONG("U0002", "密码错误");

    private final String code;
    private final String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    ResponseCode(String code, String desc)
    {
        this.code = code;
        this.desc = desc;
    }

}
