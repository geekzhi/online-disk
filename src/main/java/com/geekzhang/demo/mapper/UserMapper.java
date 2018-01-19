package com.geekzhang.demo.mapper;

import com.geekzhang.demo.orm.User;


public interface UserMapper
{
    User findByName(String name);
}
