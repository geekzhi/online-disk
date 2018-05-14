package com.geekzhang.demo.orm;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description:
 * @author: zhangpengzhi<geekzhang @ 1 6 3 . com>
 * @date: 2018/5/4 上午10:38
 * @version: V1.0
 */
@Data
public class Download {
    private int id;
    private int fileId;
    private int userId;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date time;

}
