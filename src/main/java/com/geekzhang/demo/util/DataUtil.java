package com.geekzhang.demo.util;

import org.springframework.util.StringUtils;

/**
 * @Description: 判断数据格式
 * @author: zhangpengzhi<zhang_pz @ suixingpay.com>
 * @date: 2018/2/1 下午5:57
 * @version: V1.0
 */
public class DataUtil {

    /**
     * 判断字符串是否是邮箱格式
     * @param mail
     * @return
     */
    public static Boolean isEmail(String mail){
        String regex =  "^([a-z0-9A-Z]+[-|_\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

        if(StringUtils.isEmpty(mail)) {
            return false;
        }

        if(mail.matches(regex)) {
            return true;
        }

        return false;
    }

    public static int getRandomNumber (){
        //生成四位数随机数
        int s = 0;
        for (int t = 0; t < 4; t++) {
            int i;
            do {
                i = (int) (Math.random() * 10) + 1;
            } while (i == 10);
            s = s * 10 + i;
        }
        return s;
    }


}
