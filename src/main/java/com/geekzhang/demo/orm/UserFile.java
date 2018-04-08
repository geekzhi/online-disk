package com.geekzhang.demo.orm;

import lombok.Data;
import lombok.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

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
    String parentPath; //父文件路径
    int delete; //父文件路径
    @DateTimeFormat(pattern = "yyyy-MM-dd hh-mm-ss")
    Date deleteTime; //父文件路径
    String shareCode; //分享文件识别码
    int shareValid; //分享时效， 0：不分享 1：一天内有效 7：七天内有效 2：永久分享
    String sharePass; //加密分享的密码
    @DateTimeFormat(pattern = "yyyy-MM-dd hh-mm-ss")
    Date shareTime; //分享时间
}
