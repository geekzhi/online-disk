package com.geekzhang.demo.interceptor;

import com.geekzhang.demo.redis.RedisClient;
import com.geekzhang.demo.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class CrsoInterceptor implements HandlerInterceptor {

    @Autowired
    RedisClient redisClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("进入拦截器");
        try {
            String token = request.getHeader("Authorization");
            log.info("前端传来的token为：[{}]", token);
            String usrId = TokenUtil.getUserId(token);
            log.info("usrId为：[{}]", usrId);
            if (redisClient.exists(usrId) && token.equals(redisClient.getCacheValue(usrId))) {
                log.info("token正确");
                return true;
            } else {
                log.info("token不存在");
                return false;
            }
        } catch (Exception e) {
            log.error("token验证异常", e);
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
