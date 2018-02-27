package com.geekzhang.demo.controller.api;

import com.geekzhang.demo.util.FileUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.util.List;

/**
 * @Description:
 * @author: zhangpengzhi<zhang_pz @ suixingpay.com>
 * @date: 2018/2/27 下午3:37
 * @version: V1.0
 */
@RestController
public class TestController {
    @RequestMapping(value = "/test")
    public Boolean upload(MultipartHttpServletRequest request){
        System.out.println("come in");
        List<MultipartFile> fileList = request.getFiles("file");
        System.out.println(fileList.size());
        for (MultipartFile f: fileList) {
            System.out.println(f.getOriginalFilename());
            FileUtil.uploadFile(f, "/Users/zhangpengzhi/test/");
            
        }
        return true;
    }
}
