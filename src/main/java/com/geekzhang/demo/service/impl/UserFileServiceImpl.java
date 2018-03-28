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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
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
    public Map<String, String> uploadFile(String userId, List<MultipartFile> files, String parentPath) {
        Map<String, String> map = new HashMap<>();
        for(MultipartFile file : files) {
            String fileType = file.getContentType().split("/")[0];
            if (!FileUtil.getAllowType(fileType)) {
                log.info("文件上传|不支持的文件类型");
                map.put("error", "不支持的文件类型");
                return map;
            }
            if(StringUtils.isEmpty(parentPath)) parentPath = "/";
            Map<String, Object> fileMap = FileUtil.uploadFile(file, filePath  + userId + parentPath);
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
                newFile.setParentPath(parentPath);
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
    public Map<String, Object> getFileListByType(String userId, String fileType) {
        Map<String, Object> map = new HashMap<>();
        Map<String, String> findMap = new HashMap<>();
        List<UserFile> fileList;
        findMap.put("userId", userId);
        findMap.put("type", fileType);
        log.info("获取文件|usrId:[{}],文件类型:[{}]", userId, fileType);
        if("all".equals(fileType)){
            fileList = userFileMapper.getFileList(userId);
        }else {
            fileList = userFileMapper.getFileListByType(findMap);
        }
        map.put("data", fileList);
        return map;
    }

    @Override
    public Map<String, Object> newFolder(String userId,String parentPath) {
        Map<String, Object> map = new HashMap<>();
        Map<String, String> paramMap = new HashMap<>();
        List<UserFile> list = new ArrayList<>();
        String path;
        Boolean success = false;
        int succ = 0;
        int num = 0;
        Boolean exceed = false;
        String folderName = "新建文件夹";
        if(StringUtils.isEmpty(parentPath) || "/".equals(parentPath)){
            path = filePath + userId + "/" + folderName;
            parentPath = "/";
        } else {
            path = filePath + userId + "/" + parentPath.substring(1, parentPath.length()) + "/" + folderName;
        }
        File file = new File(path);
        while(file.exists() && exceed == false){
            String newPath = path + "(" + (++num) + ")";
            file = new File(newPath);
            log.info("新建文件夹|文件夹已存在，文件号+1，路径：【{}】", newPath);
            if(num == 100) {
                exceed = true;
                log.info("新建文件夹|超出系统承受范围");
            }
        }
        if(exceed == false) {
            paramMap.put("userId", userId);
            paramMap.put("parentPath", parentPath);
            if(num != 0) {
                paramMap.put("name", folderName + "(" + num + ")");
            } else {
                paramMap.put("name", folderName);
            }
            succ = userFileMapper.insertFolder(paramMap);
            success = file.mkdirs();
            Map<String, String> fileMap = new HashMap<>();
            fileMap.put("userId", userId);
            fileMap.put("parentPath", parentPath);
            list = userFileMapper.getFileListByPath(fileMap);
        }
        log.info("新建文件夹|结果：【是否创建文件夹：{}，是否插入数据库记录：{}，是否超过系统规定次数：{}】", success, succ, exceed);
        if(true == success && succ == 1 && exceed == false){
            map.put("code", ResponseCode.SUCCESS.getCode());
            map.put("msg", ResponseCode.SUCCESS.getDesc());
            map.put("data", list);
        } else {
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }

    @Override
    public Map<String, Object> getFileListByPath(String userId, String parentPath) {
        Map<String, Object> map = new HashMap<>();
        Map<String, String> paramMap = new HashMap<>();
        if("root".equals(parentPath)) parentPath = "/";
        paramMap.put("userId", userId);
        paramMap.put("parentPath", parentPath);
        List<UserFile> list = userFileMapper.getFileListByPath(paramMap);
        map.put("code", ResponseCode.SUCCESS.getCode());
        map.put("msg", ResponseCode.SUCCESS.getDesc());
        map.put("data", list);
        return map;
    }
}
