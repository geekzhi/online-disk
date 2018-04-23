package com.geekzhang.demo.service;

import java.util.Map;

/**
 * @Description:
 * @author: zhangpengzhi<geekzhang @ 1 6 3 . com>
 * @date: 2018/4/15 下午4:13
 * @version: V1.0
 */
public interface MessageService {

    Map<String, Object> sendMess(String userId, String aimId, String userName, String mess);

    Map<String, Object> getMess(String userId, String aimId);
}
