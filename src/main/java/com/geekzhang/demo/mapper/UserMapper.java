package com.geekzhang.demo.mapper;

import com.geekzhang.demo.orm.User;

import java.util.List;
import java.util.Map;


public interface UserMapper
{
    User findByName(String name);

    List<User> findAll();

    User findByEmail(String email);

    int insert(User user);

    int changePassByEmail(Map map);
}
