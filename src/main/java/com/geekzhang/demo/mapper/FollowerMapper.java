package com.geekzhang.demo.mapper;

import com.geekzhang.demo.orm.FollowDto;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @author: zhangpengzhi<geekzhang @ 1 6 3 . com>
 * @date: 2018/4/14 下午12:35
 * @version: V1.0
 */
public interface FollowerMapper {

    List<FollowDto> select(String userId);

    int findRecorder(Map map);

    int insert(Map map);

    int delete(Map map);
}
