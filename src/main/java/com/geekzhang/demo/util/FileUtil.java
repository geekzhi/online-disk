package com.geekzhang.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @author: zhangpengzhi<zhang_pz   @   suixingpay.com>
 * @date: 2018/2/6 上午10:04
 * @version: V1.0
 */
@Slf4j
public class FileUtil {

    public static final String filePath = "/Users/zhangpengzhi/WebstormProjects/web/static/"; // 文件上传后的路径

    public static Map<String, Object> uploadFile(MultipartFile file) {
        Map<String, Object> map = new HashMap<>();
        if (file.isEmpty()) {
            map.put("isSuccess", false);
            return map;
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        log.info("文件上传|文件名：【{}】" , fileName);
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        log.info("文件上传|文件后缀：【{}】" , suffixName);
        //生成唯一标识作为文件名
        String fakeName = UuidUtil.getUuid();
        File dest = new File(filePath + fakeName + suffixName);
        log.info("文件上传|路径：【{}】", dest);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        //开始上传
        try {
            file.transferTo(dest);
            map.put("isSuccess", true);
            map.put("fileName", fileName);
            map.put("path", dest.toString());
            return map;
        } catch (Exception e) {
            map.put("isSuccess", false);
            log.info("文件上传|异常:【{}】", e);
        }
        return map;
    }
}
