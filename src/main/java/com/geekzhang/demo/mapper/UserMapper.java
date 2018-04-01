package com.geekzhang.demo.mapper;

import com.geekzhang.demo.orm.User;

import java.util.List;
import java.util.Map;


public interface UserMapper
{
    List<User> findAll();

    User findByName(String name);

    User findById(String id);

    User findByEmail(String email);

    int insert(User user);

    int changePassByEmail(Map map);

    int usePlus(Map map);
}
