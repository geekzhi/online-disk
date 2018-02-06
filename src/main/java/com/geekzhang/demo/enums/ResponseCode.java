package com.geekzhang.demo.enums;

public enum ResponseCode {
    KEY("code","msg"),
    SUCCESS("0000", "成功"),
    WRONG("1111", "系统异常"),
    REFUSE("403", "拒绝访问"),
    NO_USER("U0001", "用户名不存在"),
    PASS_WRONG("U0002", "密码错误"),
    USERNAME_REPET("U003", "用户名重复"),
    EMAIL_WRONG("R001", "邮箱格式不正确"),
    EMAIL_REPET("R002", "邮箱已被使用"),
    VERIFYCODE_WRONG("R003", "验证码错误"),
    INFO_WRONG("R004", "请检查信息是否输入有误"),
    EMAIL_NOTEXIST("R005", "邮箱不存在"),
    MODIFY_PSS_FAIL("P001", "修改失败"),
    FILE_UPLOAD_FAIL("F001", "文件上传失败");

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
