package com.geekzhang.demo.controller.api;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.geekzhang.demo.controller.AbstractController;
import com.geekzhang.demo.enums.ResponseCode;
import com.geekzhang.demo.mapper.TradeMapper;
import com.geekzhang.demo.mapper.UserMapper;
import com.geekzhang.demo.orm.PaySaPi;
import com.geekzhang.demo.orm.Trade;
import com.geekzhang.demo.orm.User;
import com.geekzhang.demo.redis.RedisClient;
import com.geekzhang.demo.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
@RequestMapping("/pay")
public class PayController extends AbstractController{

	@Value("${alipay.serverUrl}")
	private String serverUrl;

	@Value("${alipay.appId}")
	private String appId;

	@Value("${alipay.privateKey}")
	private String privateKey;

	@Value("${alipay.publicKey}")
	private String publicKey;

	@Autowired
    private RedisClient redisClient;

	@Autowired
    private TradeMapper tradeMapper;

	@Autowired
    private UserMapper userMapper;

	@GetMapping("/vip")
	public void dealVip(HttpServletRequest request, HttpServletResponse httpResponse){
	    Map<String, Object> map = new HashMap<>();
	    int time = Integer.valueOf(request.getParameter("time"));
        int price =  time * 10;
        String tradeNo = UuidUtil.getUuid();
        redisClient.setCacheValueForTime(String.valueOf(getUserId()) + "tradeNo", tradeNo, 5*60);
        redisClient.setCacheValueForTime(String.valueOf(getUserId()) + "tradeType", "vip" + time, 5*60);
		try {
			AlipayClient alipayClient = new
					DefaultAlipayClient(serverUrl, appId, privateKey, "json", "utf-8",
					publicKey, "RSA2");

			AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
			alipayRequest.setReturnUrl("http://localhost:8081/trade_info.html");
			alipayRequest.setNotifyUrl("http://106.15.183.161:8080/server/ali/back");//在公共参数中设置回跳和通知地址
            JSONObject json = new JSONObject();
            json.put("out_trade_no",tradeNo);
            json.put("product_code","FAST_INSTANT_TRADE_PAY");
            json.put("total_amount", price);
            json.put("subject","网盘会员" + time + "个月");
            json.put("body","网盘会员" + time +"个月");
            alipayRequest.setBizContent(json.toString());
			String form = "";
			form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
            Trade tradeDto = new Trade();
            tradeDto.setTradeNo(tradeNo);
            tradeDto.setUserId(Integer.valueOf(getUserId()));
            tradeDto.setPrice(Double.valueOf(price));
            tradeDto.setInfo(json.getString("subject"));
            tradeDto.setSuccess(0);
            tradeDto.setTime(new Date());
            tradeMapper.insert(tradeDto);
			httpResponse.setContentType("text/html;charset=utf-8");
			httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
			httpResponse.getWriter().flush();
			httpResponse.getWriter().close();

		} catch (Exception e) {
			log.info("支付异常：【{}】", e);
		}
	}

	@GetMapping("/expand")
    public void expand(HttpServletRequest request, HttpServletResponse httpResponse){
        Map<String, Object> map = new HashMap<>();
        int num = Integer.valueOf(request.getParameter("num"));
        int price =  num;
        String tradeNo = UuidUtil.getUuid();
        redisClient.setCacheValueForTime(String.valueOf(getUserId()) + "tradeNo", tradeNo, 5*60);
        redisClient.setCacheValueForTime(String.valueOf(getUserId()) + "tradeType", "exp" + num, 5*60);
        try {
            AlipayClient alipayClient = new
                    DefaultAlipayClient(serverUrl, appId, privateKey, "json", "utf-8",
                    publicKey, "RSA2");

            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
            alipayRequest.setReturnUrl("http://localhost:8081/trade_info.html");
            alipayRequest.setNotifyUrl("http://106.15.183.161:8080/server/ali/back");//在公共参数中设置回跳和通知地址
            JSONObject json = new JSONObject();
            json.put("out_trade_no",tradeNo);
            json.put("product_code","FAST_INSTANT_TRADE_PAY");
            json.put("total_amount", price);
            json.put("subject","扩大空间：" + num + "G");
            json.put("body","扩大空间" + num +"G");
            alipayRequest.setBizContent(json.toString());
            String form = "";
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
            Trade tradeDto = new Trade();
            tradeDto.setTradeNo(tradeNo);
            tradeDto.setUserId(Integer.valueOf(getUserId()));
            tradeDto.setPrice(Double.valueOf(price));
            tradeDto.setInfo(json.getString("subject"));
            tradeDto.setSuccess(0);
            tradeDto.setTime(new Date());
            tradeMapper.insert(tradeDto);
            httpResponse.setContentType("text/html;charset=utf-8");
            httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
            httpResponse.getWriter().flush();
            httpResponse.getWriter().close();

        } catch (Exception e) {
            log.info("支付异常：【{}】", e);
        }
    }

	@GetMapping("/info")
    @ResponseBody
    public Map<String, Object> tradeInfo(){
	    Map<String, Object> map = new HashMap<>();
	    try {
            String tradeNo = redisClient.getCacheValue(String.valueOf(getUserId()) + "tradeNo");
            if (!StringUtils.isBlank(tradeNo)) {
                String host = "http://pan.geekzhang.com:8080/server/ali/success?tradeNo=" + tradeNo;
                String path = "";
                String method = "GET";
                Map<String, String> headMap = new HashMap<>();
                Map<String, String> queryMap = new HashMap<>();
                HttpResponse response = HttpUtils.doGet(host, path, method, headMap, queryMap);
                Boolean success = Boolean.valueOf(EntityUtils.toString(response.getEntity()));
                if(success) {
                    Trade tradeDto = new Trade();
                    tradeDto.setSuccess(1);
                    tradeDto.setUserId(Integer.valueOf(getUserId()));
                    tradeDto.setTradeNo(tradeNo);
                    tradeMapper.updateSuccess(tradeDto);
                    //判断交易类型
                    String str = redisClient.getCacheValue(String.valueOf(getUserId()) + "tradeType");
                    String type = str.substring(0, 3);
                    int num = Integer.valueOf(str.substring(3, str.length()));
                    User user = userMapper.findById(getUserId());
                    if("vip".equals(type)){
                        User userDto = new User(); //更新参数DTO类
                        userDto.setId(Integer.valueOf(getUserId()));
                        Calendar calendar = Calendar.getInstance();
                        if("1".equals(user.getVip())){
                            //已经是VIP，续期
                            calendar.setTime(user.getVipExpiration());
                        } else {
                            //非VIP直接更新到期时间
                            calendar.setTime(new Date());
                            userDto.setVip("1");
                        }
                        userDto.setSize(user.getSize() + 10*1024*1024);
                        calendar.add(Calendar.MONTH, num);
                        userDto.setVipExpiration(calendar.getTime());
                        userMapper.update(userDto);
                    } else if ("exp".equals(type)) {
                        User userDto = new User();
                        long size = Long.valueOf(user.getSize())  + num * 1024 * 1024;
                        userDto.setSize(String.valueOf(size));
                        userDto.setId(user.getId());
                        userMapper.update(userDto);
                    }
                    map.put("code", ResponseCode.SUCCESS.getCode());
                    map.put("msg", ResponseCode.SUCCESS.getDesc());
                }
            }
        }catch (Exception e){
	        log.info("获取支付信息异常：【{}】", e);
        }
        return map;
    }
}
