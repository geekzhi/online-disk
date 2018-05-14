package com.geekzhang.demo.controller.api;

import com.geekzhang.demo.controller.AbstractController;
import com.geekzhang.demo.mapper.DownloadMapper;
import com.geekzhang.demo.mapper.UserFileMapper;
import com.geekzhang.demo.orm.Download;
import com.geekzhang.demo.orm.UserFile;
import com.geekzhang.demo.redis.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

/**
 * @Description:
 * @author: zhangpengzhi<geekzhang   @   1   6   3   .   com>
 * @date: 2018/3/31 下午2:43
 * @version: V1.0
 */
@Controller
@Slf4j
@RequestMapping("/nginx")
public class NginxController extends AbstractController{

    @Autowired
    private UserFileMapper userFileMapper;

    @Autowired
    private DownloadMapper downloadMapper;

    @Autowired
    private RedisClient redisClient;

    @Value("${web.var.filePath}")
    private String filePath;

    @RequestMapping("/file/{id}")
    public void passNginx(@PathVariable("id") String id, String verifyCode, final HttpServletResponse response) throws IOException {
        log.info("用户请求文件|用户名:【{}】", getUserName());
        UserFile userFile = userFileMapper.getFileById(id);
        String path = userFile.getPath();
        String name = userFile.getName() + userFile.getSuffixName();
        File file = new File(filePath+path);
        if (file != null && file.exists()) {
            log.info("文件存在，转发至Nginx");
            if(String.valueOf(userFile.getUserId()).equals(getUserId())) {
                log.info("获取文件为本人");
                xAccelRedirectFile(file, response, path, name);
            }else{
                String trueVerifyCode = redisClient.getCacheValue("shareFileVerifyCode" + getUserId());
                log.info("获取文件非本人，识别码：【{}】,redis中存储的识别码：【{}】", verifyCode, trueVerifyCode);
                if(!StringUtils.isEmpty(trueVerifyCode) && !StringUtils.isEmpty(verifyCode) && trueVerifyCode.equals(verifyCode)){
                    userFileMapper.updateDownloadTimes(id);
                    Download dto = new Download();
                    dto.setFileId(Integer.valueOf(id));
                    dto.setUserId(Integer.valueOf(getUserId()));
                    dto.setTime(new Date());
                    downloadMapper.insert(dto);
                    xAccelRedirectFile(file, response, path, name);
                } else {
                    log.info("非法请求");
                    response.sendError(404);
                }
            }
        } else {
            log.info("文件不存在,路径：【{}】", file.getPath());
            response.sendError(404);
        }
    }

    protected void xAccelRedirectFile(File file, HttpServletResponse response, String path, String name) throws UnsupportedEncodingException {
        String encoding = response.getCharacterEncoding();
        response.setHeader("Content-Type", "application/octet-stream;");
        response.setHeader("X-Accel-Redirect", "/file/" +
                URLEncoder.encode(path,"UTF-8"));
        response.setHeader("X-Accel-Charset", "utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" +
                URLEncoder.encode(name,"UTF-8")
        );
        System.out.println(response.getHeader("X-Accel-Redirect"));
        System.out.println(response.getHeader("Content-Disposition"));
    }

}
