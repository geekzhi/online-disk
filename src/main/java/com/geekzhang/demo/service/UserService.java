package com.geekzhang.demo.service;

import com.geekzhang.demo.orm.User;

import java.util.Map;

public interface UserService {

    Map<String, Object> getUserInfo(String userId);

    Map<String, Object> login(User user);

    Map<String, Object> register(User user);

    Map<String, Object> sendVerifyCode(String email);

    Map<String, Object> forgotPass(String email);

    Map<String, Object> changePass(String id, String newPass);

    Map<String, Object> logout(String userId);

    Map<String, Object> changeAvatar(String image, String userId);
}
