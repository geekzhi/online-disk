package com.geekzhang.demo.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @Description:
 * @author: zhangpengzhi<zhang_pz @ suixingpay.com>
 * @date: 2018/2/6 下午2:10
 * @version: V1.0
 */
public interface UserFileService {
    Map<String, Object> uploadFile(String userId, MultipartFile file);

    Map<String, Object> getFileList(String userId);
}
