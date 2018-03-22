package com.geekzhang.demo.orm;

import lombok.Data;

/**
 * @Description:
 * @author: zhangpengzhi<zhang_pz @ suixingpay.com>
 * @date: 2018/2/6 下午2:00
 * @version: V1.0
 */
@Data
public class UserFile {
    int id;
    String name;  //用户定义文件名
    String suffixName; //文件后缀名
    String path; //文件路径
    String type; //文件类型 img:图片 video:视频
    int userId;  //文件所属用户ID
    String size; //文件大小，单位：Kb
}
