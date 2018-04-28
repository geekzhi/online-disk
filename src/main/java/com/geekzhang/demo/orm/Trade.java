package com.geekzhang.demo.orm;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description:
 * @author: zhangpengzhi<geekzhang @ 1 6 3 . com>
 * @date: 2018/4/24 下午2:42
 * @version: V1.0
 */
@Data
public class Trade {
    private int id;
    private String tradeNo;
    private int userId;
    private Double price;
    private String info;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date time;
    private int success; //交易成功为1，交易失败(未支付)为0
}
