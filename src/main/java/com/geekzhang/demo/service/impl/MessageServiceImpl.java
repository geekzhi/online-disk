package com.geekzhang.demo.service.impl;

import com.geekzhang.demo.enums.ResponseCode;
import com.geekzhang.demo.redis.RedisClient;
import com.geekzhang.demo.service.MessageService;
import com.geekzhang.demo.util.ContentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @author: zhangpengzhi<geekzhang @ 1 6 3 . com>
 * @date: 2018/4/15 下午4:13
 * @version: V1.0
 */

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    RedisClient redisClient;

    /**
     * 发送消息
     * @param userId
     * @param aimId
     * @param userName
     * @param mess
     * @return
     */
    @Override
    public Map<String, Object> sendMess(String userId, String aimId, String userName, String mess) {
        Map<String, Object> map = new HashMap<>();
        String content;
        try {
            if(!ContentUtil.pass(mess)) {
                map.put("code", ResponseCode.TEXT_SENSITIVE.getCode());
                map.put("msg", ResponseCode.TEXT_SENSITIVE.getDesc());
                return map;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if(redisClient.exists("chat" + userId + "and" + aimId) ||
                redisClient.exists("chat" + aimId + "and" + userId)) {
             content = redisClient.getCacheValue("chat" + userId + "and" + aimId)
                    + "\n" + userName + ":" + mess;
        } else {
             content = userName + ":" + mess;
        }
        redisClient.setCacheValueForTime("chat" + userId + "and" + aimId, content, 24*60*60);
        redisClient.setCacheValueForTime("chat" + aimId + "and" + userId, content, 24*60*60);
        map.put("code", ResponseCode.SUCCESS.getCode());
        map.put("msg", ResponseCode.SUCCESS.getDesc());
        return map;
    }

    /**
     * 获取消息
     * @param userId
     * @param aimId
     * @return
     */
    @Override
    public Map<String, Object> getMess(String userId, String aimId) {
        Map<String, Object> map = new HashMap<>();
        String mess = "";
        if(redisClient.exists("chat" + userId + "and" + aimId) ||
                redisClient.exists("chat" + aimId + "and" + userId)){
             mess = redisClient.getCacheValue("chat" + userId + "and" + aimId);
        }
        map.put("code", ResponseCode.SUCCESS.getCode());
        map.put("msg", ResponseCode.SUCCESS.getDesc());
        map.put("data", mess);
        return map;
    }
}
