package com.geekzhang.demo.mapper;

import com.geekzhang.demo.orm.Friends;
import com.geekzhang.demo.orm.User;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @author: zhangpengzhi<geekzhang @ 1 6 3 . com>
 * @date: 2018/4/14 下午6:52
 * @version: V1.0
 */
public interface FriendsMapper {
    List<User> selectFriends(Map map);

    List<User> selectUnPassFriends(Map map);

    List<Friends> findFriend(Map map);

    int addFriendSingle(Map map);

    int dealFriend(Map map);
}
