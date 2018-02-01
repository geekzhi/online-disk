package com.geekzhang.demo.controller.api;

import com.geekzhang.demo.enums.ResponseCode;
import com.geekzhang.demo.orm.User;
import com.geekzhang.demo.redis.RedisClient;
import com.geekzhang.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    RedisClient redisClient;

    /**
     * 用户登录
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    public Map login(User user) {
        Map<String, Object> map = new HashMap<>();
        try {
            map = userService.login(user);
        } catch (Exception e) {
            log.error("用户登录异常", e);
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }

    @RequestMapping(value = "/register", method = {RequestMethod.POST})
    public Map register(User user) {
        Map<String , Object> map = new HashMap<>();
        try{
            map = userService.register(user);
        } catch (Exception e) {
            log.error("注册异常", e);
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }

    @RequestMapping(value = "/verifyCode", method = {RequestMethod.POST})
    public Map sendVerifyCode(String email){
        Map<String, Object> map = new HashMap<>();
        try {
            map = userService.sendVerifyCode(email);
        } catch (Exception e) {
            log.error("发送验证码异常", e);
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }

//    @RequestMapping(value = "test/{email}")
//    public Map sendEmail(@PathVariable String email){
//
//    }
}



