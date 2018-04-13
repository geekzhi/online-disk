package com.geekzhang.demo.orm;

import lombok.Data;

@Data
public class User {
    int id;
    String name; //用户名
    String pass; //密码
    String email; //邮箱
    String verifyCode; //验证码
    String remember; //过期时间
    String vip; //1:vip 0:非vip
    String size; //网盘大小 Kb
    String use; //已使用大小 Kb
    String pic; //头像地址
}
