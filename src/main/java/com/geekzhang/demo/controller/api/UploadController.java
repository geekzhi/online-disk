package com.geekzhang.demo.controller.api;

import com.geekzhang.demo.controller.AbstractController;
import com.geekzhang.demo.enums.ResponseCode;
import com.geekzhang.demo.service.UserFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;


/**
 * @Description: 文件上传
 * @author: zhangpengzhi<zhang_pz @ suixingpay.com>
 * @date: 2018/2/6 上午9:56
 * @version: V1.0
 */

@RestController
@Slf4j
public class UploadController extends AbstractController {

    @Autowired
    private UserFileService userFileService;

    @RequestMapping(value = "/file", method = {RequestMethod.POST})
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile file){
        Map<String, Object> map = new HashMap<>();
        try {
            String userId = getUserId();
            map = userFileService.uploadFile(userId, file);
        } catch (Exception e) {
            log.info("文件上传|异常：【{}】", e);
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }

    @RequestMapping(value = "/fileList", method = {RequestMethod.GET})
    public Map<String, Object> getFileList() {
        Map<String, Object> map = new HashMap<>();
        try {
            String userId = getUserId();
            map = userFileService.getFileList(userId);
        } catch (Exception e) {
            log.info("获取用户文件|异常：【{}】", e);
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }
}
