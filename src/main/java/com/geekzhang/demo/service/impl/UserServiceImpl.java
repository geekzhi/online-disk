package com.geekzhang.demo.service.impl;

import com.geekzhang.demo.enums.ResponseCode;
import com.geekzhang.demo.mapper.UserMapper;
import com.geekzhang.demo.orm.User;
import com.geekzhang.demo.redis.RedisClient;
import com.geekzhang.demo.service.UserService;
import com.geekzhang.demo.util.DataUtil;
import com.geekzhang.demo.util.EmailUtil;
import com.geekzhang.demo.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    public Map<String, Object> register(User user){
        Map<String, Object> map = new HashMap<>();
        if(!StringUtils.isEmpty(user.getName())
            && !StringUtils.isEmpty(user.getEmail())
                && !StringUtils.isEmpty(user.getName())
                && !StringUtils.isEmpty(user.getPass())
                && DataUtil.isEmail(user.getEmail())
        ) {
        User u1 = userMapper.findByName(user.getName());
        User u2 = userMapper.findByEmail(user.getEmail());
        if(u1 != null ) {
            log.info("注册|用户名重复");
            map.put("code", ResponseCode.USERNAME_REPET.getCode());
            map.put("msg", ResponseCode.USERNAME_REPET.getDesc());
            return map;
        }
        if(u2 != null) {
            log.info("注册|邮箱重复");
            map.put("code", ResponseCode.EMAIL_REPET.getCode());
            map.put("msg", ResponseCode.EMAIL_REPET.getDesc());
            return map;
        }
        String verifyCode = redisClient.getCacheValue(user.getEmail());
        if(!StringUtils.isEmpty(verifyCode) && verifyCode.equals(user.getVerifyCode())) {
            log.info("注册|注册成功：[{}]", user.toString());
            userMapper.insert(user);
            map.put("code", ResponseCode.SUCCESS.getCode());
            map.put("code", ResponseCode.SUCCESS.getDesc());
        } else {
            log.info("注册|验证码错误");
            map.put("code", ResponseCode.VERIFYCODE_WRONG.getCode());
            map.put("msg", ResponseCode.VERIFYCODE_WRONG.getDesc());
            return map;
        }
        } else {
            log.info("注册|信息填写错误");
            map.put("code", ResponseCode.INFO_WRONG.getCode());
            map.put("msg", ResponseCode.INFO_WRONG.getDesc());
            return map;
        }
        return map;
    }

    public Map<String, Object> sendVerifyCode(String email) {
        Map<String, Object> map = new HashMap<>();
        if(DataUtil.isEmail(email)){
            log.info("发送验证码|[{}]邮箱格式正确",email);
            User user = userMapper.findByEmail(email);
            if(user == null) {
                log.info("发送验证码|[{}]邮箱没有注册", email);
                String subject = "注册验证码";
                String msg = "";
                String verifyCode = DataUtil.getRandomNumber();
                msg = "您的验证码为：" + verifyCode + ",五分钟内有效。";
                log.info("发送验证码|开始向[{}]发送邮件", email);
                EmailUtil.sendEmail(email, msg, subject);
                redisClient.setCacheValueForTime(email, verifyCode, 5 * 60);
                log.info("发送验证码|发送成功，已存入验证码[{}]", verifyCode);
                map.put("code", ResponseCode.SUCCESS.getCode());
                map.put("msg", ResponseCode.SUCCESS.getDesc());
            } else {
                log.info("发送验证码|[{}]邮箱重复", email);
                map.put("code", ResponseCode.EMAIL_REPET.getCode());
                map.put("msg", ResponseCode.EMAIL_REPET.getDesc());
            }
        } else {
            log.info("发送验证码|[{}]邮箱格式不正确", email);
            map.put("code", ResponseCode.EMAIL_WRONG.getCode());
            map.put("msg", ResponseCode.EMAIL_WRONG.getDesc());
        }
        return map;
    }
}
