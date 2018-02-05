package com.geekzhang.demo.service;

import com.geekzhang.demo.orm.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    Map<String, Object> login(User user);

    List<User> getAll();

    Map<String, Object> register(User user);

    Map<String, Object> sendVerifyCode(String email);

    Map<String, Object> forgotPass(String email);

    Map<String, Object> changePass(String id, String newPass);
}
