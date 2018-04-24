package com.geekzhang.demo.controller.api;


import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.geekzhang.demo.mapper.FriendsMapper;
import com.geekzhang.demo.redis.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description:
 * @author: zhangpengzhi<zhang_pz @ suixingpay.com>
 * @date: 2018/2/27 下午3:37
 * @version: V1.0
 */
@RestController
public class TestController {

    @Autowired
    RedisClient redisClient;

    @RequestMapping(value = "/test")
    public void get(HttpServletResponse httpResponse) throws Exception {
        String serverUrl = "https://openapi.alipaydev.com/gateway.do";
        String appId = "2016091500518177";
        String privateKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDDULruGd97XNTzaM0wd7tX4e8Xt4/ICizk9EQqzDVtNlTB/TC1G6i0w1kMfIq9Ptj96g4UM9dcq7eE/j4WWMQcUYx+1M4uS1ZQNM13tOi4i0LkkG55Jm8EMo4Rl0RyJ0gV9ze4mrPlS/S7IKPBS0Wv8pdRn5Y9btUa1AQtqkgARbudBN/AT9U3gaszXqZrVOrNMSruY5MRvgnmDxtlwv2ENVOndLfqdHIqVSNbAm708SrNBwaGBfuVHhzOBBzBqfZq342Bflt7uoJBBupyscBZ9xCiKSKz9IPwkAUZOg2PEJnQaovHlG3c51HbchrPhbzrQ8zOnt6m9bXWCoQCHw+9AgMBAAECggEBAJqUHh2rZHAh/HfYjey6Fg9rxOqkYKsVQorBt+GXVQFmapZNaGstxMurf6KePr+gKeRrhVD/5oZ1cP1UqpbNEgjHkcYfbj69aUZ5Q9Rl3XmQ3hQ/Ku+/N0W/LSlG6ujdpfOcgQEdjxr+jmY7Yt7sT+78tD3pa+KbARYGjngFuI5dPwefwDknlVrqlNzQ/BheJNl4gxT6E51w89tMLFXwrMbtgmJepOwz3Zp+5PzwJcQo2+r6BbZr6LRWLysRQFd6DhJSHBcY720usHFfTdaJ04epVtnWtmaqn/2dPXwTF3D/HNtPgtHrOljE7eJ0sJPYlh7NTSbfw8G8FOO72rToCQECgYEA8rlu+IRuD18YR//dQfx4DFU/E4mH2uEf66mg5rLB5Ubh4e8DhvZ/EBqb3WQAISmGYgjGOnjbOOVxsavRamFIMmoD9VYGSMZSmqlmZ97+kdNbM6My2YMNB3vdILH8arLMZAnw6zCzA046AybjRvry65Egkm7exgH2s3VMxwxG4y0CgYEAzf981MUxAawcJ/8jsLYK+zck8fR6i1t0tjtTJAvC33ZZvE7VuR9E/s36n46L1gQLkO+UAgt2D/oCXBVsMIv2GkGSkzVf6mdko55rbTTg9EdASRb7GhQsiDOP/faWcKKgLkz6eb+8u8cVrCtBo60LHk+jMtnuhHhhCYaLUtxc+NECgYEAyMq70VyES7UIOi+hEsBPcyxmFLx2JYyMwoA9+fqwygHcRPJbwZ9St7i7becW8FgQsjK+AYsPFz8/0ZHomI9HkWUAR0lVw4hEt2shqxXfFDV7QflkzGlMMv+1iyGSRZYJ3UwYAAf2XuqstQTLiWF56YPSJTEQm07ujcCgth0i2zUCgYEAt0TwCFQPj49R1Xu2gwbSqkGbSu/Y/pa1NVN+WnlkPG8M0u3IU1Swi4kxyjaFzjf3GlEres47kWc6Jjag9Gfw4dco0nMQDHlEHhVZOeVrpaimVuBtMfx0yhAlsSprGXZ1z/uIdDzTarRqYaxyT24qkCMF7L3a6Fw46MKcvXFz/aECgYBmDrycynp5IPiCNPQPfLZIRVIwymnlNACEnQLU4bIpCX4jI9keiv6PXMlModWj1XK8rb4ov20DdIWVYxwnFpu/zX/6nklBNnZ6PJBdFhaEJN5PSvfwqRPWeyHn1/ieT60oQd3c1+MhMfceAMQMUA+RYxU8qkflBIECarfr6M6Pwg==";
        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw1C67hnfe1zU82jNMHe7V+HvF7ePyAos5PREKsw1bTZUwf0wtRuotMNZDHyKvT7Y/eoOFDPXXKu3hP4+FljEHFGMftTOLktWUDTNd7TouItC5JBueSZvBDKOEZdEcidIFfc3uJqz5Uv0uyCjwUtFr/KXUZ+WPW7VGtQELapIAEW7nQTfwE/VN4GrM16ma1TqzTEq7mOTEb4J5g8bZcL9hDVTp3S36nRyKlUjWwJu9PEqzQcGhgX7lR4czgQcwan2at+NgX5be7qCQQbqcrHAWfcQoikis/SD8JAFGToNjxCZ0GqLx5Rt3OdR23Iaz4W860PMzp7epvW11gqEAh8PvQIDAQAB/cIUPNsXia22korioWiNgyn2fMQPb7fL2vu7Ah62h7BsPX4OxE9XJrMRYbM3/NKeRE0RjsTxi/uZeDHUqN8GzbiugK+baTCwevmC9K0owQ1HoltoM3UovlPIvZ8qieidbANeG1MLuI8eygL9EBw9X1Zdu0Rjdug+3OGYEu4s0dlpgw4ES/HjBixoysjuM5jMWW7si+juOq+VFbHgdDOdA7eyxwVuackgUFob+TPUNdWAzk0MUih3L6CJBOuAHrXcBebayAmnRdsLUa8OG8ytc2x1H9sDiAeeNJCbGY6ir4+HUDYfWLNvLcps3PW5j0gQNu9wIDAQAB";


        AlipayClient alipayClient = new
                DefaultAlipayClient(serverUrl,appId,privateKey,"json","utf-8",
                publicKey,"RSA2" );

        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        alipayRequest.setReturnUrl("http://www.baidu.com");
        alipayRequest.setNotifyUrl("http://106.15.183.161:8080/server/ali/back");//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent("{" +
                "    \"out_trade_no\":\"201503200101010ss3\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":88.88," +
                "    \"subject\":\"Iphone6 16G\"," +
                "    \"body\":\"Iphone6 16G\"," +
                "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\"2088511833207846\"" +
                "    }"+
                "  }");//填充业务参数
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=utf-8");
        httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();

    }

}