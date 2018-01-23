package com.geekzhang.demo.orm;

import lombok.Data;

@Data
public class User {
    int id;
    String name; //用户名
    String pass; //密码
    String remember; //过期时间
}
