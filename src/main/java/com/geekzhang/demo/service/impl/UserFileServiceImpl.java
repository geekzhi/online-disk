package com.geekzhang.demo.service.impl;

import com.geekzhang.demo.enums.ResponseCode;
import com.geekzhang.demo.mapper.UserFileMapper;
import com.geekzhang.demo.mapper.UserMapper;
import com.geekzhang.demo.orm.User;
import com.geekzhang.demo.orm.UserFile;
import com.geekzhang.demo.redis.RedisClient;
import com.geekzhang.demo.service.UserFileService;
import com.geekzhang.demo.util.DataUtil;
import com.geekzhang.demo.util.FileUtil;
import com.geekzhang.demo.util.UuidUtil;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description:
 * @author: zhangpengzhi<zhang_pz               @               suixingpay.com>
 * @date: 2018/2/6 下午2:10
 * @version: V1.0
 */
@Service
@Slf4j
public class UserFileServiceImpl implements UserFileService {

    @Autowired
    private UserFileMapper userFileMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisClient redisClient;

    @Value("${web.var.filePath}")
    private String filePath;

    @Value("${web.var.splitPath}")
    private String splitPath;

    @Value("${web.var.pageSize}")
    private String pageSize;

    @Value("${web.var.share}")
    private String shareUrl;

    /**
     * 上传文件
     *
     * @param userId
     * @param files
     * @param parentPath
     * @return
     */
    @Override
    public Map<String, String> uploadFile(String userId, List<MultipartFile> files, String parentPath) {
        Map<String, String> map = new HashMap<>();
        for (MultipartFile file : files) {
            String fileType = file.getContentType().split("/")[0];
//            if (!FileUtil.getAllowType(fileType)) {
//                log.info("文件上传|不支持的文件类型");
//                map.put("error", "不支持的文件类型");
//                return map;
//            }
            /**
             * 判断是否超过用户网盘容量
             */
            User user = userMapper.findById(userId);
            if (Long.valueOf(user.getSize()) < Long.valueOf(user.getUse()) + file.getSize() / 1024) {
                log.info("文件上传|文件大小超过用户容量");
                map.put("error", "网盘容量不足，请扩容");
                return map;
            }
            if (StringUtils.isEmpty(parentPath)) parentPath = "/";
            Map<String, Object> fileMap = FileUtil.uploadFile(file, filePath + userId + parentPath + "/");
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
                Map<String, String> usePlusMap = new HashMap<>();
                usePlusMap.put("userId", userId);
                usePlusMap.put("size", newFile.getSize());
                int i = userMapper.usePlus(usePlusMap);
                System.out.println(newFile.toString());
                int j = userFileMapper.insert(newFile);
                log.info("i:{},j{}", i, j);
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

    /**
     * 通过文件类型获取文件列表
     *
     * @param userId
     * @param fileType
     * @param pageNum
     * @return
     */
    @Override
    public Map<String, Object> getFileListByType(String userId, String fileType, String pageNum) {
        Map<String, Object> map = new HashMap<>();
        Map<String, String> findMap = new HashMap<>();
        List<UserFile> fileList;
        int total = 0;
        findMap.put("userId", userId);
        findMap.put("type", fileType);
        log.info("获取文件|usrId:[{}],文件类型:[{}]", userId, fileType);
        //请求的是回收站中文件
        if ("trash".equals(fileType)) {
            total = userFileMapper.getTotalTrash(userId);
            PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
            fileList = userFileMapper.getTrashFileList(userId);
        } else {
            total = userFileMapper.getTotalByType(findMap);
            PageHelper.startPage(Integer.valueOf(pageNum), Integer.valueOf(pageSize));
            fileList = userFileMapper.getFileListByType(findMap);
        }
        map.put("data", fileList);
        map.put("page", (total / Integer.valueOf(pageSize)) + 1);
        return map;
    }

    /**
     * 新建文件夹
     *
     * @param userId
     * @param parentPath
     * @return
     */
    @Override
    public Map<String, Object> newFolder(String userId, String parentPath) {
        Map<String, Object> map = new HashMap<>();
        Map<String, String> paramMap = new HashMap<>();
        List<UserFile> list = new ArrayList<>();
        String path;
        Boolean success = false;
        int succ = 0;
        int num = 0;
        Boolean exceed = false;
        String folderName = "新建文件夹";
        if (StringUtils.isEmpty(parentPath) || "/".equals(parentPath)) {
            path = filePath + userId + "/" + folderName;
            parentPath = "/";
        } else {
            path = filePath + userId + "/" + parentPath.substring(1, parentPath.length()) + "/" + folderName;
        }
        File file = new File(path);
        while (file.exists() && exceed == false) {
            String newPath = path + "(" + (++num) + ")";
            file = new File(newPath);
            log.info("新建文件夹|文件夹已存在，文件号+1，路径：【{}】", newPath);
            if (num == 100) {
                exceed = true;
                log.info("新建文件夹|超出系统承受范围");
            }
        }
        if (exceed == false) {
            paramMap.put("userId", userId);
            paramMap.put("parentPath", parentPath);
            if (num != 0) {
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
        if (true == success && succ == 1 && exceed == false) {
            map.put("code", ResponseCode.SUCCESS.getCode());
            map.put("msg", ResponseCode.SUCCESS.getDesc());
            map.put("data", list);
        } else {
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }

    /**
     * 通过文件路径获取文件列表
     *
     * @param userId
     * @param parentPath
     * @return
     */
    @Override
    public Map<String, Object> getFileListByPath(String userId, String parentPath) {
        Map<String, Object> map = new HashMap<>();
        Map<String, String> paramMap = new HashMap<>();
        if ("root".equals(parentPath)) parentPath = "/";
        paramMap.put("userId", userId);
        paramMap.put("parentPath", parentPath);
        List<UserFile> list = userFileMapper.getFileListByPath(paramMap);
        map.put("code", ResponseCode.SUCCESS.getCode());
        map.put("msg", ResponseCode.SUCCESS.getDesc());
        map.put("data", list);
        return map;
    }

    /**
     * 删除文件（伪删除）
     *
     * @param id
     * @param type
     * @return
     */
    @Override
    public Map<String, Object> deleteFile(String id, String type) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> paraMap = new HashMap<>();
        int success = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, 10);

        paraMap.put("id", id);
        paraMap.put("deleteFlag", 1);
        paraMap.put("deleteTime", calendar.getTime());
        success = userFileMapper.updateFileDeleteById(paraMap);
        if (success > 0) {
            log.info("删除文件|成功");
            map.put("code", ResponseCode.SUCCESS.getCode());
            map.put("msg", ResponseCode.SUCCESS.getDesc());
        } else {
            log.info("删除文件|失败");
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }

    /**
     * 修改文件名
     *
     * @param fileId
     * @param fileNewName
     * @param fileType
     * @param userId
     * @return
     */
    @Override
    public Map<String, Object> modifyFileName(String fileId, String fileNewName, String fileType, String userId) {
        Map<String, Object> map = new HashMap<>();
        Map<String, String> paraMap = new HashMap<>();
        int success = 0;
        paraMap.put("id", fileId);
        paraMap.put("userId", userId);
        if ("folder".equals(fileType)) {
            paraMap.put("suffixName", fileNewName);
            success = userFileMapper.modifyFolderName(paraMap);
        } else {
            paraMap.put("name", fileNewName);
            success = userFileMapper.modifyFileNameById(paraMap);
        }
        if (success > 0) {
            map.put("code", ResponseCode.SUCCESS.getCode());
            map.put("msg", ResponseCode.SUCCESS.getDesc());
        } else {
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }

    @Override
    public Map<String, Object> shareFile(String fileId, String shareTime, String shareType, String userId) {
        Map<String, Object> map = new HashMap<>();
        Map<String, String> paraMap = new HashMap<>();
        int success = 0;
        String sharePass = "";
        String code = UuidUtil.getUuid();
        String shareValid = "0";
        if ("encrypt".equals(shareType)) {
            sharePass = String.valueOf(DataUtil.getRandomNumber());
        }
        if (!"forever".equals(shareTime)) {
            paraMap.put("id", fileId);
            paraMap.put("userId", userId);
            //redis存入数据
            long time = 0l;
            if ("seven".equals(shareTime)) {
                time = 7 * 24 * 60 * 60;
                shareValid = "7";
            }
            if ("one".equals(shareTime)) {
                time = 24 * 60 * 60;
                shareValid = "1";
            }
            redisClient.setCacheValueForTime("shareFile" + fileId, code, time);
        } else {
            shareValid = "2";
        }
        //数据库操作
        paraMap.put("id", fileId);
        paraMap.put("userId", userId);
        paraMap.put("shareValid", shareValid);
        paraMap.put("shareCode", code);
        paraMap.put("sharePass", sharePass);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
        paraMap.put("shareTime", sdf.format(new Date()));
        success = userFileMapper.updateShareFile(paraMap);
        if (success > 0 && !StringUtils.isEmpty(code)) {
            String url = shareUrl + code;
            map.put("code", ResponseCode.SUCCESS.getCode());
            map.put("msg", ResponseCode.SUCCESS.getDesc());
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("url", url);
            dataMap.put("pass", sharePass);
            map.put("data", dataMap);
        } else {
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }

    @Override
    public Map<String, Object> shareDownload(String code, String pass, String userId) {
        Map<String, Object> map = new HashMap<>();
        Map<String, String> dataMap = new HashMap<>();
        UserFile userFile = userFileMapper.getFileByShareCode(code);
        //数据库中没有分享文件的记录
        if (null == userFile) {
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
            return map;
        }
        //非永久分享并且redis中无分享码
        if (2 != userFile.getShareValid()
                && StringUtils.isEmpty(redisClient.getCacheValue("shareFile" + userFile.getId()))) {
            log.info("文件分享|文件非永久分享并且redis中无分享码");
            map.put("code", ResponseCode.FILE_SHARE_TIMEOUT.getCode());
            map.put("msg", ResponseCode.FILE_SHARE_TIMEOUT.getDesc());
            return map;
        }
        //文件分享存在
        if (!StringUtils.isEmpty(userFile.getSharePass()) && StringUtils.isEmpty(pass)) {
            log.info("文件分享|文件为加密分享");
            dataMap.put("encrypt", "yes");
        } else {
            if (!StringUtils.isEmpty(pass) && !pass.equals(userFile.getSharePass())) {
                map.put("code", ResponseCode.FILE_SHARE_PASS_WRONG.getCode());
                map.put("msg", ResponseCode.FILE_SHARE_PASS_WRONG.getDesc());
                map.put("data", dataMap);
                return map;
            } else {
                dataMap.put("url", String.valueOf(userFile.getId()));
                Date date = userFile.getShareTime();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dataMap.put("time", sdf.format(date));
                int valid = userFile.getShareValid();
                if ("1".equals(String.valueOf(valid)) || "7".equals(String.valueOf(valid))) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.DATE, valid);
                    Calendar nowDate = Calendar.getInstance();
                    nowDate.setTime(new Date());
                    dataMap.put("valid", String.valueOf(calendar.get(Calendar.DATE) - nowDate.get(Calendar.DATE)));
                }
                dataMap.put("fileName", userFile.getName() + userFile.getSuffixName());
                String verifyCode = UuidUtil.getUuid();
                redisClient.setCacheValueForTime("shareFileVerifyCode" + userId, verifyCode, 24 * 60 * 60);
                dataMap.put("verifyCode", verifyCode);
            }
        }
        map.put("code", ResponseCode.SUCCESS.getCode());
        map.put("msg", ResponseCode.SUCCESS.getDesc());
        map.put("data", dataMap);
        return map;
    }
}
