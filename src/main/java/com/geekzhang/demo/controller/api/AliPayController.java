package com.geekzhang.demo.controller.api;

import com.geekzhang.demo.redis.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 该类仅用于支付宝沙箱服务端回调使用
 * @author: zhangpengzhi<geekzhang @ 1 6 3 . com>
 * @date: 2018/4/24 上午10:23
 * @version: V1.0
 */
@RestController
@Slf4j
@RequestMapping("/server/ali")
public class AliPayController {

    @Autowired
    private RedisClient redisClient;

    @PostMapping("/back")
    public void getAliBack(HttpServletRequest request){
        log.info("支付宝回调信息：【{}】", request.toString());
        log.info("支付宝交易号：【{}】", request.getParameter("trade_no"));
        String tradeNo = request.getParameter("out_trade_no");
        log.info("商户订单号：【{}】", tradeNo);
        redisClient.setCacheValueForTime(tradeNo, "success", 5*60);
    }

    @GetMapping("/success")
    public Boolean getStatus(String tradeNo){
        if(redisClient.exists(tradeNo)) {
            redisClient.delCacheByKey(tradeNo);
            return true;
        } else {
            return false;
        }
    }
}
