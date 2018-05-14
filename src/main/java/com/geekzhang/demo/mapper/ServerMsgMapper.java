package com.geekzhang.demo.mapper;

import com.geekzhang.demo.orm.ServerMsg;

import java.util.List;

/**
 * @Description:
 * @author: zhangpengzhi<geekzhang @ 1 6 3 . com>
 * @date: 2018/5/5 下午3:40
 * @version: V1.0
 */
public interface ServerMsgMapper {
    List<ServerMsg> select();
}
