package com.geekzhang.demo.orm;

import lombok.Data;

/**
 * @Description:
 * @author: zhangpengzhi<zhang_pz @ suixingpay.com>
 * @date: 2018/2/9 上午11:33
 * @version: V1.0
 */
@Data
public class DataVo {
    private Header header;
    private Data data;
    @lombok.Data
    public class Data{
        private String name;
        private String pass;
    }
    @lombok.Data
    public class Header {
        private String prtVer;
        private String appVer;
        private String sysType;
    }

}

