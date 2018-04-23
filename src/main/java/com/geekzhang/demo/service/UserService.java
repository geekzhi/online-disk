package com.geekzhang.demo.service;

import com.geekzhang.demo.orm.User;

import java.util.Map;

public interface UserService {

    Map<String, Object> getUserInfo(String userId);

    Map<String, Object> login(User user);

    Map<String, Object> register(User user);

    Map<String, Object> sendVerifyCode(String email);

    Map<String, Object> forgotPass(String email);

    Map<String, Object> changePass(String id, String newPass);

    Map<String, Object> logout(String userId);

    Map<String, Object> changeAvatar(String image, String userId);

    Map<String, Object> follow(String id, String userId);

    Map<String, Object> getFriendsList(String userId);

    Map<String, Object> addFriends(String userId, String name);

    Map<String, Object> getFriendNotice(String userId);

    Map<String, Object> dealFriend(String userId, String friendId, Boolean agree);

    Map<String, Object> changeName(String userId, String newName);

    Map<String, Object> changeOldPass(String userId, String oldPass, String newPass);

    Map<String, Object> confirgEmailVerify(String userId, String email, String code);
}
