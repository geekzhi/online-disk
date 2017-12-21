package com.geekzhang.demo.dao;

import com.geekzhang.demo.orm.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper
{
    List<User> findAll();
    int getIdByName(String name);
}
