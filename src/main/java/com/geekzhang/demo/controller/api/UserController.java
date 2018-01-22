package com.geekzhang.demo.controller.api;

import com.geekzhang.demo.controller.AbstractController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @author: zhangpengzhi<zhang_pz @ suixingpay.com>
 * @date: 2018/1/22 上午11:23
 * @version: V1.0
 */

@RestController
@Slf4j
public class UserController extends AbstractController{

    /**
     * 自动登录校验
     * @return
     */
    @RequestMapping(value = "/user/username", method = {RequestMethod.POST})
    public String getUserInfo(){
        String username =  getUserName();
        log.info("用户名为：[{}]", username);
        return username;
    }
}
