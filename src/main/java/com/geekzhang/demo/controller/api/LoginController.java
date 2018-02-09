package com.geekzhang.demo.controller.api;

import com.geekzhang.demo.enums.ResponseCode;
import com.geekzhang.demo.orm.DataVo;
import com.geekzhang.demo.orm.User;
import com.geekzhang.demo.redis.RedisClient;
import com.geekzhang.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
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

    /**
     * 用户注册
     * @param user
     * @return
     */
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

    /**
     * 注册发送邮箱验证码
     * @param email
     * @return
     */
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

    @RequestMapping(value = "/forgot", method = {RequestMethod.POST})
    public Map forgotPass (String email) {
        Map<String, Object> map = new HashMap<>();
        try{
            map = userService.forgotPass(email);
        } catch (Exception e) {
            log.info("找回密码异常", e);
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }

    @RequestMapping(value = "/changePass", method = {RequestMethod.POST})
    public Map changePass (String id, String newPass){
        Map<String, Object> map = new HashMap<>();
        try{
            map = userService.changePass(id, newPass);
        } catch (Exception e) {
            log.info("修改密码异常", e);
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }
    @RequestMapping(value = "/test", method = {RequestMethod.POST})
    public Map sendEmail(@RequestBody DataVo data){
        Map map = new HashMap();
//        JSONObject json = JSONObject.parseObject(dataMap.get("data").toString());
        log.info("test|"+ data.toString());
        log.info(data.getHeader().getAppVer());
        log.info(data.getData().getName());
        log.info(data.getData().getPass());
//        if("1".equals(json.get("name")) && "1".equals(json.get("pass"))) {
//            map.put("msg","ok");
//        } else {
//           map.put("msg", "fail");
//        }
        return map;
    }
}



