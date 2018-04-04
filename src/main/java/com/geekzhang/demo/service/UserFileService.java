package com.geekzhang.demo.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @author: zhangpengzhi<zhang_pz @ suixingpay.com>
 * @date: 2018/2/6 下午2:10
 * @version: V1.0
 */
public interface UserFileService {

    Map<String, String> uploadFile(String userId, List<MultipartFile> files, String parentPath);

    Map<String, Object> getFileListByType(String userId, String fileType, String pageNum);

    Map<String, Object> newFolder(String userId, String parentPath);

    Map<String, Object> getFileListByPath(String userId, String parentPath);

    Map<String, Object> deleteFile(String id, String type);

    Map<String, Object> modifyFileName(String fileId, String fileNewName, String fileType, String userId);
}
