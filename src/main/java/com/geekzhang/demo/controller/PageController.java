package com.geekzhang.demo.controller;

import com.geekzhang.demo.orm.User;
import com.geekzhang.demo.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PageController {

    @Autowired
    UserServiceImpl userService;

    @RequestMapping("/")
    public String index(){
        return "Hello Worlds";
    }


    @RequestMapping("list")
    public List<User> list(){
        return userService.findAll();
    }

    @RequestMapping("getId/{name}")
    public int getId(@PathVariable(value = "name") String name){
        return userService.getIdByName(name);
    }
}
