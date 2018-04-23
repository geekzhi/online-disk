package com.geekzhang.demo.controller.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geekzhang.demo.controller.AbstractController;
import com.geekzhang.demo.enums.ResponseCode;
import com.geekzhang.demo.service.UserFileService;
import com.geekzhang.demo.util.TokenUtil;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

    /**
     * 上传文件
     * @param request
     * @param parentPath
     * @return
     */
    @RequestMapping(value = "/upload", method = {RequestMethod.POST})
    public Map<String, String> uploadFile(MultipartHttpServletRequest request, String parentPath){
        Map<String, String> map = new HashMap<>();
        try {
            String userId = TokenUtil.getUserId(request().getParameter("token"));
            map = userFileService.uploadFile(userId, request.getFiles("files"), parentPath);
        } catch (Exception e) {
            log.info("文件上传|异常：【{}】", e);
            map.put("error", "系统内部异常");
        }
        return map;
    }

    /**
     * 通过文件类型获取文件
     * @param fileType
     * @param pageNum
     * @return
     */
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

    /**
     * 新建文件夹
     * @param parentPath
     * @return
     */
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

    /**
     * 删除文件
     * @param id
     * @param type
     * @return
     */
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

    /**
     * 修改文件名
     * @param fileId
     * @param fileNewName
     * @param fileType
     * @return
     */
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

    /**
     * 获取文件统计
     * @return
     */
    @RequestMapping(value = "/statistics", method = {RequestMethod.GET})
    public Map<String, Object> getStatistics(){
        Map<String, Object> map = new HashMap<>();
        try {
            log.info("获取文件统计|用户ID：【{}】", getUserId());
            map = userFileService.getStatistics(getUserId());
        }catch (Exception e) {
            log.info("获取文件统计|异常：【{}】", e);
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }

    /**
     * 恢复选中文件
     * @param
     * @return
     */
    @PutMapping("/recover")
    public Map<String, Object> recoverChoose(String id){
        Map<String, Object> map = new HashMap<>();
        try {
            String userId = getUserId();
            log.info("恢复选中文件|用户ID：【{}】，文件id:[{}]", userId, id);
            map = userFileService.recoverChoose(userId, id);
        }catch (Exception e) {
            log.info("恢复选中文件|异常：【{}】", e);
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;
    }

    /**
     * 恢复所有文件
     * @param
     * @return
     */
    @PutMapping("/recoverAll")
    public Map<String, Object> recoverAll(){
        Map<String, Object> map = new HashMap<>();
        try {
            String userId = getUserId();
            log.info("恢复所有文件|用户ID：【{}】", userId);
            map = userFileService.recoverAll(userId);
        }catch (Exception e) {
            log.info("恢复所有文件|异常：【{}】", e);
            map.put("code", ResponseCode.WRONG.getCode());
            map.put("msg", ResponseCode.WRONG.getDesc());
        }
        return map;

    }
}
