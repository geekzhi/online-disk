package com.geekzhang.demo.controller.api;

import com.geekzhang.demo.controller.AbstractController;
import com.geekzhang.demo.enums.ResponseCode;
import com.geekzhang.demo.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @author: zhangpengzhi<geekzhang @ 1 6 3 . com>
 * @date: 2018/4/15 下午4:11
 * @version: V1.0
 */
@RestController
@Slf4j
@RequestMapping(value = "/mess")
public class MessageController extends AbstractController{

    @Autowired
    private MessageService messageService;

    /**
     * 用户发送消息
     * @param aimId
     * @param mess
     * @return
     */
    @RequestMapping(value = "/send", method = { RequestMethod.POST})
    public Map<String, Object> sendMess(String aimId, String mess){
        Map<String, Object> map = new HashMap<>();
        try {
            String userId = getUserId();
            String userName = getUserName();
            log.info("发送消息，用户ID:[{}]，目标ID[{}]", userId, aimId);
            map = messageService.sendMess(userId, aimId, userName, mess);
        } catch (Exception e) {
            log.error("发送消息异常", e);
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }

    @RequestMapping(value = "/getMess", method = { RequestMethod.POST})
    public Map<String, Object> getMess(String aimId){
        Map<String, Object> map = new HashMap<>();
        try {
            String userId = getUserId();
            log.info("获取用户消息，用户ID:[{}]，目标ID[{}]", userId, aimId);
            map = messageService.getMess(userId, aimId);
        } catch (Exception e) {
            log.error("获取用户消息异常", e);
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }
}
