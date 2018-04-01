package com.geekzhang.demo.mapper;

import com.geekzhang.demo.orm.UserFile;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @author: zhangpengzhi<zhang_pz @ suixingpay.com>
 * @date: 2018/2/6 下午1:59
 * @version: V1.0
 */
public interface UserFileMapper {
    int insert(UserFile userFile);

    List<UserFile> getFileList(String userId);

    List<UserFile> getFileListByType(Map map);

    int insertFolder(Map map);

    List<UserFile> getFileListByPath(Map map);

    UserFile getFileById(String id);
}
