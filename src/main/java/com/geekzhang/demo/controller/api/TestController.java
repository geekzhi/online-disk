package com.geekzhang.demo.controller.api;


import com.geekzhang.demo.mapper.FriendsMapper;
import com.geekzhang.demo.redis.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @Description:
 * @author: zhangpengzhi<zhang_pz @ suixingpay.com>
 * @date: 2018/2/27 下午3:37
 * @version: V1.0
 */
@RestController
public class TestController {

    @Autowired
    RedisClient redisClient;

    @RequestMapping(value = "/test")
    public String get() throws Exception {

        return "1";

    }
}