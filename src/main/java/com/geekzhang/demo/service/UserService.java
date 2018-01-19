package com.geekzhang.demo.service;

import com.geekzhang.demo.orm.User;

import java.util.Map;

public interface UserService {
    Map<String, Object> login(User user);
}
