package com.geekzhang.demo.controller.api;

import com.geekzhang.demo.controller.AbstractController;
import com.geekzhang.demo.enums.ResponseCode;
import com.geekzhang.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @author: zhangpengzhi<geekzhang @ 1 6 3 . com>
 * @date: 2018/4/20 下午1:13
 * @version: V1.0
 */
@RestController
@Slf4j
@RequestMapping("/notice")
public class NoticeController extends AbstractController{

    @Autowired
    UserService userService;

    @GetMapping("/friend")
    public Map<String, Object> getFriendNotice(){
        Map<String, Object> map = new HashMap<>();
        try {
            String userId = getUserId();
            log.info("获取用户好友申请列表，用户ID:[{}]", userId);
            map = userService.getFriendNotice(userId);
        } catch (Exception e) {
            log.error("获取用户好友申请列表", e);
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }

    @GetMapping("/friend/{friendId}/{agree}")
    public Map<String, Object> agreeFiend(@PathVariable String friendId, @PathVariable Boolean agree) {
        Map<String, Object> map = new HashMap<>();
        try {
            String userId = getUserId();
            log.info("批准好友申请，用户ID:[{}]，申请人ID:[{}], 是否同意：【{}】", userId, friendId, agree);
            map = userService.dealFriend(userId, friendId, agree);
        } catch (Exception e) {
            log.error("批准好友申请", e);
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }
}
