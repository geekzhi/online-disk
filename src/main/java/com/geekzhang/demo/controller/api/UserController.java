package com.geekzhang.demo.controller.api;

import com.geekzhang.demo.controller.AbstractController;
import com.geekzhang.demo.enums.ResponseCode;
import com.geekzhang.demo.orm.User;
import com.geekzhang.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @author: zhangpengzhi<zhang_pz @ suixingpay.com>
 * @date: 2018/1/22 上午11:23
 * @version: V1.0
 */

@RestController
@Slf4j
@RequestMapping(value = "/user")
public class UserController extends AbstractController{

    @Autowired
    private UserService userService;
    /**
     * 自动登录校验
     * @return
     */
    @RequestMapping(value = "/userInfo", method = {RequestMethod.POST})
    public Map<String, Object> getUserInfo(){
        Map<String, Object> map = new HashMap<>();
        try {
            User u = new User();
            u.setName(getUserName());
            u.setVip(getUserVip());
            u.setSize(getUserSize());
            u.setUse(getUserUse());
            log.info("用户信息为：[{}]", u.toString());
            if(null != u.getName() && null != u.getVip() && null != u.getSize() && null!=u.getUse()) {
                map.put("code", ResponseCode.SUCCESS.getCode());
                map.put("msg", ResponseCode.SUCCESS.getDesc());
                map.put("data", u);
            } else {
                throw new Exception() ;
            }
        } catch (Exception e){
            log.error("获取用户信息异常", e);
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }

    @RequestMapping(value = "/logout", method = {RequestMethod.GET})
    public Map<String, Object> logout(){
        Map<String, Object> map = new HashMap<>();
        try {
            String userId = getUserId();
            map = userService.logout(userId);
        } catch (Exception e) {
            log.error("登出异常", e);
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }
}
