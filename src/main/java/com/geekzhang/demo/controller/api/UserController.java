package com.geekzhang.demo.controller.api;

import com.geekzhang.demo.controller.AbstractController;
import com.geekzhang.demo.enums.ResponseCode;
import com.geekzhang.demo.mapper.FollowerMapper;
import com.geekzhang.demo.mapper.UserMapper;
import com.geekzhang.demo.orm.FollowDto;
import com.geekzhang.demo.orm.User;
import com.geekzhang.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FollowerMapper followerMapper;

    /**
     * 自动登录校验
     * @return
     */
    @RequestMapping(value = "/userInfo", method = {RequestMethod.POST})
    public Map<String, Object> getUserInfo(){
        Map<String, Object> map = new HashMap<>();
        try {
            map = userService.getUserInfo(getUserId());
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

    @RequestMapping(value = "/changeAvatar", method = {RequestMethod.POST})
    public Map<String, Object> changeAvatar(String image) {
        Map<String, Object> map = new HashMap<>();
        try {
            String userId = getUserId();
            map = userService.changeAvatar(image, userId);
        } catch (Exception e) {
            log.error("修改头像异常", e);
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }

    /**
     * 添加关注，如果已经关注则取消关注
     * @param id
     * @return
     */
    @RequestMapping(value = "/follow/{id}", method = {RequestMethod.POST})
    public Map<String, Object> followSomeone(@PathVariable String id) {
        Map<String, Object> map = new HashMap<>();
        try {
            String userId = getUserId();
            map = userService.follow(id, userId);
        } catch (Exception e) {
            log.error("添加关注异常", e);
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }

    @GetMapping("/follow")
    public Map<String, Object> getFollow(){
        Map<String, Object> map = new HashMap<>();
        try {
            String userId = getUserId();
            List<FollowDto> list = followerMapper.select(userId);
            map.put("code", ResponseCode.SUCCESS.getCode());
            map.put("msg", ResponseCode.SUCCESS.getDesc());
            map.put("data", list);
        } catch (Exception e) {
            log.error("获取关注者分享文件异常", e);
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }

    /**
     * 获取好友列表
     * @return
     */
    @RequestMapping(value = "/friends", method = {RequestMethod.POST})
    public Map<String, Object> getFriendsList (){
        Map<String, Object> map = new HashMap<>();
        try {
            String userId = getUserId();
            log.info("开始获取好友列表，用户ID:[{}]", userId);
            map = userService.getFriendsList(userId);
        } catch (Exception e) {
            log.error("获取好友列表异常", e);
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }

    /**
     * 添加好友
     * @param name
     * @return
     */
    @GetMapping("/addFriends")
    public Map<String, Object> addFriends(String name){
        Map<String, Object> map = new HashMap<>();
        try {
            String userId = getUserId();
            log.info("添加好友，用户ID:[{}]，要添加的用户名或邮箱：【{}】", userId, name);
            map = userService.addFriends(userId, name);
        } catch (Exception e) {
            log.error("添加好友异常", e);
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }

    /**
     * 修改用户名
     * @param newName
     * @return
     */
    @PutMapping("/name/{newName}")
    public Map<String, Object> changeName(@PathVariable String newName) {
        Map<String, Object> map = new HashMap<>();
        try {
            String userId = getUserId();
            log.info("修改用户名，用户ID:[{}]，新用户名：【{}】", userId, newName);
            map = userService.changeName(userId, newName);
        } catch (Exception e) {
            log.error("修改用户名异常", e);
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }

    /**
     * 修改邮箱发送验证码
     * @param oldPass
     * @param newPass
     * @return
     */
    @PutMapping("/pass/{oldPass}/{newPass}")
    public Map<String, Object> changeOldPass(@PathVariable String oldPass, @PathVariable String newPass) {
        Map<String, Object> map = new HashMap<>();
        try {
            String userId = getUserId();
            log.info("修改密码，用户ID:[{}]，旧密码：【{}】，新密码：【{}】", userId, oldPass, newPass);
            map = userService.changeOldPass(userId, oldPass, newPass);
        } catch (Exception e) {
            log.error("修改密码异常", e);
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }

    /**
     * 验证新邮箱验证码
     * @param code
     * @return
     */
    @PostMapping("/newEmail")
    public Map<String, Object> confirgEmailVerify(String email, String code){
        Map<String, Object> map = new HashMap<>();
        try {
            String userId = getUserId();
            log.info("验证新邮箱验证码，用户ID:[{}]，新邮箱：【{}】，用户输入验证码：【{}】", userId, email, code);
            map = userService.confirgEmailVerify(userId, email, code);
        } catch (Exception e) {
            log.error("验证新邮箱验证码异常", e);
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }

    @PostMapping("/newFace")
    public Map<String, Object> changeFace (String faceImg) {
        Map<String, Object> map = new HashMap<>();
        try {
            String userId = getUserId();
            log.info("更换人脸，用户ID:[{}]", userId, faceImg);
            map = userService.changeFace(userId, faceImg);
        } catch (Exception e) {
            log.error("更换人脸", e);
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }

    @PutMapping("/readNotice")
    public void readNotice(){
        User uDto = new User();
        uDto.setId(Integer.valueOf(getUserId()));
        uDto.setNotice(0);
        userMapper.update(uDto);
    }
}
