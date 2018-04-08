package com.geekzhang.demo.controller.api;

import com.geekzhang.demo.controller.AbstractController;
import com.geekzhang.demo.enums.ResponseCode;
import com.geekzhang.demo.service.UserFileService;
import com.geekzhang.demo.util.TokenUtil;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
    public Map<String, String> uploadFile(MultipartHttpServletRequest request, String parentPath){
        Map<String, String> map = new HashMap<>();
        try {
            System.out.println();
            System.out.println("-----    " + parentPath);
            System.out.println();
            System.out.println();
            String userId = TokenUtil.getUserId(request().getParameter("token"));
            map = userFileService.uploadFile(userId, request.getFiles("files"), parentPath);
        } catch (Exception e) {
            log.info("文件上传|异常：【{}】", e);
            map.put("error", "系统内部异常");
        }
        return map;
    }

    @RequestMapping(value = "/fileList/{fileType}/{pageNum}", method = {RequestMethod.GET})
    public Map<String, Object> getFileListByType(@PathVariable String fileType, @PathVariable String pageNum) {
        Map<String, Object> map = new HashMap<>();
        try {
            String userId = getUserId();
            map = userFileService.getFileListByType(userId, fileType, pageNum);
        } catch (Exception e) {
            log.info("获取用户文件|异常：【{}】", e);
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }

    @RequestMapping(value = "/folder", method = {RequestMethod.POST})
    public Map<String, Object> newFolder(@RequestParam String parentPath) {
        Map<String, Object> map = new HashMap<>();
        try {
            map = userFileService.newFolder(getUserId(), parentPath);
        } catch (Exception e) {
            log.info("新建文件夹|异常：【{}】", e);
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }

    @RequestMapping(value = "/fileList/path", method = RequestMethod.POST)
    public Map<String, Object> getFileListByPath (@RequestParam String parentPath){
       Map<String, Object> map = new HashMap<>();
       try {
           log.info("根据文件父路径获取文件|父路径：【{}】", parentPath);
           String userId = getUserId();
           map = userFileService.getFileListByPath(userId, parentPath);
       }catch (Exception e) {
           log.info("根据文件父路径获取文件|异常：【{}】", e);
           map.put("code", ResponseCode.WRONG.getCode());
           map.put("msg", ResponseCode.WRONG.getDesc());
       }
       return map;
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    public Map<String, Object> deleteFile (@RequestParam String id, @RequestParam String type) {
        Map<String, Object> map = new HashMap<>();
        try {
            log.info("删除文件|文件ID：【{}】，类型：【{}】", id, type );
            map = userFileService.deleteFile(id, type);

        }catch (Exception e) {
            log.info("删除文件|异常：【{}】", e);
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }

    @RequestMapping(value = "/{fileId}/{fileNewName}/{fileType}", method = {RequestMethod.PUT})
    public Map<String, Object> modifyFileName (@PathVariable String fileId, @PathVariable String fileNewName, @PathVariable String fileType) {
        Map<String, Object> map = new HashMap<>();
        try {
            log.info("修改文件名|文件id:【{}】, 新的文件名：【{}】, 文件类型：【{}】", fileId, fileNewName, fileType);
            map = userFileService.modifyFileName(fileId, fileNewName, fileType, getUserId());
        }catch (Exception e) {
            log.info("修改文件名|异常：【{}】", e);
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }

        return map;
    }

    /**
     * 分享文件
     */
    @RequestMapping(value = "/share", method = {RequestMethod.POST})
    public Map<String, Object> shareFile (@RequestParam String id, @RequestParam String shareTime, @RequestParam String shareType){
        Map<String, Object> map = new HashMap<>();
        try {
            log.info("分享文件|文件id:【{}】, 分享类型：【{}】, 有效期限：【{}】", id, shareType, shareTime);
            map = userFileService.shareFile(id, shareTime, shareType, getUserId());
        }catch (Exception e) {
            log.info("分享文件|异常：【{}】", e);
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }

    /**
     * 获取分享文件信息
     */
    @RequestMapping(value = "/shareDownload", method = {RequestMethod.POST})
    public Map<String, Object> shareDownload(String code, String pass){
        Map<String, Object> map = new HashMap<>();
        try {
            log.info("获取分享文件信息|分享码：【{}】", code);
            map = userFileService.shareDownload(code, pass, getUserId());
        }catch (Exception e) {
            log.info("获取分享文件信息|异常：【{}】", e);
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }
}
