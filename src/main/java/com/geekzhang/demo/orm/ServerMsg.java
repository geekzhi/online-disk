package com.geekzhang.demo.orm;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description:
 * @author: zhangpengzhi<geekzhang @ 1 6 3 . com>
 * @date: 2018/5/5 下午3:36
 * @version: V1.0
 */
@Data
public class ServerMsg {
    private int id;
    private String msg;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date pulltime;
}
