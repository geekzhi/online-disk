package com.geekzhang.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

/**
 * @Description:
 * @author: zhangpengzhi<zhang_pz   @   suixingpay.com>
 * @date: 2018/2/6 上午10:04
 * @version: V1.0
 */
@Slf4j
public class FileUtil {

    public static Map<String, Object> uploadFile(MultipartFile file, String filePath) {
        Map<String, Object> map = new HashMap<>();
        if (file.isEmpty()) {
            map.put("isSuccess", false);
            return map;
        }
        // 获取文件名
        String fileOriginalName = file.getOriginalFilename();
        String fileName = fileOriginalName.substring(0, fileOriginalName.lastIndexOf("."));
        log.info("文件上传|文件名：【{}】" , fileName);
        // 获取文件的后缀名
        String suffixName = fileOriginalName.substring(fileOriginalName.lastIndexOf("."));
        log.info("文件上传|文件后缀：【{}】" , suffixName);
        //生成唯一标识作为文件名
        String fakeName = UuidUtil.getUuid();
        File dest = new File(filePath + fakeName + suffixName);
        log.info("文件上传|路径：【{}】", dest);
        String size = String.valueOf(file.getSize() / 1024);
        log.info("文件上传|文件大小：【{}】", size + "M");
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        //开始上传
        try {
            file.transferTo(dest);
            map.put("isSuccess", true);
            map.put("fileName", fileName);
            map.put("suffixName", suffixName);
            map.put("path", dest.toString());
            map.put("size", size);
            return map;
        } catch (Exception e) {
            map.put("isSuccess", false);
            log.info("文件上传|异常:【{}】", e);
        }
        return map;
    }

    public static Boolean videoType(String type){
        String[] videoType = {"mp4", "flv"};
        if(Arrays.toString(videoType).contains(type)) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean imgType(String type){
        String[] imgTypes = {"png", "jpg", "gif", "bmp", "jepg"};
        if(Arrays.toString(imgTypes).contains(type)) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean docType(String type){
        String[] docType = {".doc", ".excel", ".ppt", ".pdf"};
        if(Arrays.toString(docType).contains(type)) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean getAllowType(String type) {
        System.out.println(type);
        if("video".equals(type) || "image".equals(type)) {
            return true;
        } else {
            return false;
        }
    }
}
