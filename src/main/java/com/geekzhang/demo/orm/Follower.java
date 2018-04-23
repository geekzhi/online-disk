package com.geekzhang.demo.orm;

import lombok.Data;

/**
 * @Description:
 * @author: zhangpengzhi<geekzhang @ 1 6 3 . com>
 * @date: 2018/4/13 下午4:51
 * @version: V1.0
 */
@Data
public class Follower {
    private int id;
    private int userId;
    private int followId;
}
