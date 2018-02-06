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

    public static String getRandomNumber (){
        int i = (int)(Math.random()*100)*100 + (int)(Math.random()*10);
        return String.valueOf(i);
    }


}
