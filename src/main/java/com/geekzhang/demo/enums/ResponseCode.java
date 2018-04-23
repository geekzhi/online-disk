package com.geekzhang.demo.enums;

public enum ResponseCode {
    KEY("code","msg"),
    SUCCESS("0000", "成功"),
    WRONG("1111", "系统异常"),
    REFUSE("403", "拒绝访问"),
    NO_USER("U0001", "用户名不存在"),
    PASS_WRONG("U0002", "密码错误"),
    USERNAME_REPET("U003", "用户名重复"),
    REPEAT_PASS_WRONG("U0004", "旧密码错误"),
    EMAIL_WRONG("R001", "邮箱格式不正确"),
    EMAIL_REPET("R002", "邮箱已被使用"),
    VERIFYCODE_WRONG("R003", "验证码错误"),
    INFO_WRONG("R004", "请检查信息是否输入有误"),
    EMAIL_NOTEXIST("R005", "邮箱不存在"),
    MODIFY_PSS_FAIL("P001", "修改失败"),
    FILE_UPLOAD_FAIL("F001", "文件上传失败"),
    FILE_TYPE_WRONG("F002","不支持的文件类型"),
    FILE_NAME_REPET("F003", "文件名重复"),
    FILE_SHARE_TIMEOUT("F004", "文件分享已失效"),
    FILE_SHARE_PASS_WRONG("F005", "文件分享密码错误"),
    FRIEND_SEND_ADDMESS("F006", "已发送添加好友消息,请勿重复发送"),
    FRIEND_REPET("F007", "已经在好友列表中"),
    FRIEND_SELF("F008", "请勿添加自己为好友"),
    FOLLOWER_CANCEL("N001", "取消关注"),
    TEXT_SENSITIVE("T001", "文字内容违法"),;

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
