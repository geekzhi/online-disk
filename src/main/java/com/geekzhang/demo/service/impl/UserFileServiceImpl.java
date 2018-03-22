package com.geekzhang.demo.service.impl;

import com.geekzhang.demo.enums.ResponseCode;
import com.geekzhang.demo.mapper.UserFileMapper;
import com.geekzhang.demo.orm.UserFile;
import com.geekzhang.demo.service.UserFileService;
import com.geekzhang.demo.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @author: zhangpengzhi<zhang_pz @ suixingpay.com>
 * @date: 2018/2/6 下午2:10
 * @version: V1.0
 */
@Service
@Slf4j
public class UserFileServiceImpl implements UserFileService {

    @Autowired
    private UserFileMapper userFileMapper;

    @Value("${web.var.filePath}")
    private String filePath;

    @Value("${web.var.splitPath}")
    private String splitPath;

    @Override
    public Map<String, String> uploadFile(String userId, List<MultipartFile> files) {
        Map<String, String> map = new HashMap<>();
        for(MultipartFile file : files) {
            String fileType = file.getContentType().split("/")[0];
            if (!FileUtil.getAllowType(fileType)) {
                log.info("文件上传|不支持的文件类型");
                map.put("error", "不支持的文件类型");
                return map;
            }
            Map<String, Object> fileMap = FileUtil.uploadFile(file, filePath + "/" + userId +"/");
            if ((Boolean) fileMap.get("isSuccess")) {
                log.info("文件上传|成功");
                UserFile newFile = new UserFile();
                newFile.setName((String) fileMap.get("fileName"));
                String filePath = (String) fileMap.get("path");
                filePath = filePath.split(splitPath)[1];
                newFile.setPath(filePath);
                newFile.setType(fileType);
                newFile.setSuffixName((String) fileMap.get("suffixName"));
                newFile.setUserId(Integer.valueOf(userId));
                newFile.setSize((String) fileMap.get("size"));
                userFileMapper.insert(newFile);
                log.info("文件上传|已存入userId：【{}】的文件：【{}】", userId, newFile.getName());
                return map;
            } else {
                log.info("文件上传|失败");
                map.put("error", "文件上传失败");
                return map;
            }
        }
        return map;
    }

    @Override
    public Map<String, Object> getFileList(String userId, String fileType) {
        Map<String, Object> map = new HashMap<>();
        Map<String, String> findMap = new HashMap<>();
        findMap.put("userId", userId);
        findMap.put("type", fileType);
        log.info("获取文件|usrId:[{}],文件类型:[{}]", userId, fileType);
        List<UserFile> fileList = userFileMapper.getFileListByType(findMap);
        map.put("data", fileList);
        return map;
    }
}
