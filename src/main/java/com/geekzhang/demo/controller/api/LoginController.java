package com.geekzhang.demo.controller.api;

import com.geekzhang.demo.orm.User;
import com.geekzhang.demo.redis.RedisClient;
import com.geekzhang.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    RedisClient redisClient;

    /**
     * 用户登录
     * @param user
     * @return
     */
    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    public Map login(User user){
        Map<String, Object> map = userService.login(user);
        return map;
    }

//    @RequestMapping(value = "/login/all", method = {RequestMethod.GET})
//    public List<User> login(){
//        List<User> userList = userService.getAll();
//        System.out.println("111");
//        return userList;
//    }

}
