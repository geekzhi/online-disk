package com.geekzhang.demo.controller.api;

import com.geekzhang.demo.controller.AbstractController;
import com.geekzhang.demo.mapper.UserFileMapper;
import com.geekzhang.demo.orm.UserFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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

    @Value("${web.var.filePath}")
    private String filePath;

    protected static final String DEFAULT_FILE_ENCODING = "ISO-8859-1";


    @RequestMapping("/file/{id}")
    public void passNginx(@PathVariable("id") String id, final HttpServletResponse response) throws IOException {
        log.info("用户请求文件|用户名:【{}】", getUserName());
        UserFile userFile = userFileMapper.getFileById(id);
        String path = userFile.getPath();
        String name = userFile.getName() + userFile.getSuffixName();
        File file = new File(filePath+path);
        if (file != null && file.exists()) {
            log.info("文件存在，转发至Nginx");
            xAccelRedirectFile(file, response, path, name);
        } else {
            log.info("文件不存在");
            response.sendError(404);
        }
    }

    protected void xAccelRedirectFile(File file, HttpServletResponse response, String path, String name) throws UnsupportedEncodingException {
        String encoding = response.getCharacterEncoding();
        response.setHeader("Content-Type", "application/octet-stream;");
        response.setHeader("X-Accel-Redirect", "/file/" + toPathEncoding(encoding, path));
        response.setHeader("X-Accel-Charset", "utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" +
                URLEncoder.encode(name,"UTF-8")
        );
        System.out.println(response.getHeader("X-Accel-Redirect"));
        System.out.println(response.getHeader("Content-Disposition"));
//        response.setContentLength((int) file.contentLength());
    }

    //如果存在中文文件名或中文路径需要对其进行编码成 iSO-8859-1
    //否则会导致 nginx无法找到文件及弹出的文件下载框也会乱码
    private String toPathEncoding(String origEncoding, String fileName) throws UnsupportedEncodingException {
        return new String(fileName.getBytes(origEncoding), DEFAULT_FILE_ENCODING);
    }

}
