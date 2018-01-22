package com.geekzhang.demo.controller.api;

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
public class UserController {

    /**
     * 自动登录校验
     * @return
     */
    @RequestMapping(value = "/user/info", method = {RequestMethod.POST})
    public String getUserInfo(){
        return "1";
    }
}
