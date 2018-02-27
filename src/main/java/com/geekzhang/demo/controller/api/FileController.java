package com.geekzhang.demo.controller.api;

import com.geekzhang.demo.controller.AbstractController;
import com.geekzhang.demo.enums.ResponseCode;
import com.geekzhang.demo.service.UserFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

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
@RequestMapping("/file")
public class FileController extends AbstractController {

    @Autowired
    private UserFileService userFileService;

    @RequestMapping(value = "/upload", method = {RequestMethod.POST})
    public Boolean uploadFile(MultipartHttpServletRequest request){
        Boolean success = false;
        try {
            String userId = getUserId();
            success = userFileService.uploadFile(userId, request.getFiles("files"));
        } catch (Exception e) {
            log.info("文件上传|异常：【{}】", e);
        }
        return success;
    }

    @RequestMapping(value = "/fileList/{fileType}", method = {RequestMethod.GET})
    public Map<String, Object> getFileList(@PathVariable String fileType) {
        Map<String, Object> map = new HashMap<>();
        try {
            String userId = getUserId();
            map = userFileService.getFileList(userId, fileType);
        } catch (Exception e) {
            log.info("获取用户文件|异常：【{}】", e);
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }
}
