package com.geekzhang.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.HtmlEmail;

/**
 * @Description:
 * @author: zhangpengzhi<zhang_pz @ suixingpay.com>
 * @date: 2018/2/1 下午3:26
 * @version: V1.0
 */
@Slf4j
public class EmailUtil {
    public static void sendEmail(String mail, String msg, String subject){
    HtmlEmail email = new HtmlEmail();
        try {
        // 这里是SMTP发送服务器的名字：163的如下："smtp.163.com"
        email.setHostName("smtp.163.com");
        // 字符编码集的设置
        email.setCharset("utf-8");
        // 发送人的邮箱
        email.setFrom("geekzhang@163.com", "geekzhang");
        //设置认证：用户名-密码
        email.setAuthentication("geekzhang@163.com","ss2063963");
        // 设置收件人信息
        email.addTo(mail);
        //设置邮件主题
        email.setSubject(subject);
        // 要发送的信息，可以使用HTML标签
        email.setHtmlMsg(msg);
        // 发送
        email.send();
    } catch (Exception e) {
        log.error("发送邮件异常",e);
    }
    }
}
