package com.geekzhang.demo.service.impl;

import com.geekzhang.demo.enums.ResponseCode;
import com.geekzhang.demo.mapper.UserMapper;
import com.geekzhang.demo.orm.User;
import com.geekzhang.demo.redis.RedisClient;
import com.geekzhang.demo.service.UserService;
import com.geekzhang.demo.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisClient redisClient;

    @Override
    public Map<String, Object> login(User user) {
        Map<String, Object> map = new HashMap<>();
        String name = user.getName();
        String pass = user.getPass();
        String remember = user.getRemember();
        User resultUser = userMapper.findByName(name);
        if (resultUser == null) {
            log.info("用户登录|用户名[{}]不存在", name);
            map.put("code", ResponseCode.NO_USER.getCode());
            map.put("msg", ResponseCode.NO_USER.getDesc());
        } else {
            if (pass.equals(resultUser.getPass())) {
                //生成token
                Map<String, Object> claim = new HashMap<>();
                String usrId = String.valueOf(resultUser.getId());
                claim.put("usrName", resultUser.getName());
                claim.put("usrId", resultUser.getId());
                String token = TokenUtil.getJWTString(usrId, claim);
                log.info("用户登录|用户名密码正确，生成token:[{}]", token);
                if("on".equals(remember)) {
                    redisClient.setCacheValueForTime(usrId, token, 5*60);
                } else {
                    redisClient.setCacheValueForTime(usrId, token, 24*60*60);
                }
                log.info("用户登录|已向redis存入用户token");
                map.put("code", ResponseCode.SUCCESS.getCode());
                map.put("msg", ResponseCode.SUCCESS.getDesc());
                map.put("data", token);
            } else {
                map.put("code", ResponseCode.PASS_WRONG.getCode());
                map.put("msg", ResponseCode.PASS_WRONG.getDesc());
            }
        }
        return map;
    }

    @Override
    public List<User> getAll() {
        return userMapper.findAll();
    }
}
