package com.geekzhang.demo.orm;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd hh-mm-ss")
    Date vipExpiration; //vip过期时间
    String face; //人脸识别图像
    int notice; //是否有通知消息 1：有 0：无
    int staus; //是否停用 1：停用 0：正常
}
