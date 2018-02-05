package com.geekzhang.demo.util;

import java.util.UUID;

/**
 * @Description:
 * @author: zhangpengzhi<zhang_pz @ suixingpay.com>
 * @date: 2018/2/2 上午10:04
 * @version: V1.0
 */
public class UuidUtil {
    public static String getUuid () {
        return UUID.randomUUID().toString().replace("-","");
    }
}
