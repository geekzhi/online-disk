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

    int getTotalByType(Map map);

    int getTotalTrash(String userId);

    int insert(UserFile userFile);

    int getFileNumByParentPath(String parentPath);

    List<UserFile> getFileList(String userId);

    List<UserFile> getFileListByType(Map map);

    List<UserFile> getTrashFileList(String userId);

    int insertFolder(Map map);

    List<UserFile> getFileListByPath(Map map);

    UserFile getFileById(String id);

    int updateFileDeleteById(Map map);

    int updateFolderDeleteById(Map map);

    int modifyFileNameById(Map map);

    int modifyFolderName(Map map);

}
