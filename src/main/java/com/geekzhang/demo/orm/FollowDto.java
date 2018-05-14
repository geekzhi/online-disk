package com.geekzhang.demo.orm;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description:
 * @author: zhangpengzhi<geekzhang @ 1 6 3 . com>
 * @date: 2018/5/6 上午11:11
 * @version: V1.0
 */
@Data
public class FollowDto {
    private String name;
    private String pic;
    private String fname;
    private String shareCode;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh-mm-ss")
    private Date shareTime;
}
