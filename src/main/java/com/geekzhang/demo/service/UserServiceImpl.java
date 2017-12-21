package com.geekzhang.demo.service;

import com.geekzhang.demo.dao.UserMapper;
import com.geekzhang.demo.orm.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl {

    @Autowired
    UserMapper userMapper;

    public List<User> findAll(){
        return userMapper.findAll();
    }

    public int getIdByName(String name){
        return userMapper.getIdByName(name);
    }
}
