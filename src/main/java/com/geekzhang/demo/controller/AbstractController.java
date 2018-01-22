package com.geekzhang.demo.controller;

import com.geekzhang.demo.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description:
 * @author: zhangpengzhi<zhang_pz   @   suixingpay.com>
 * @date: 2018/1/22 下午1:48
 * @version: V1.0
 */
@Slf4j
public class AbstractController {

    private static ThreadLocal<ServletRequest> REQUESTTHREADLOCAL = new ThreadLocal<>();

    private static ThreadLocal<ServletResponse> RESPONSETHREADLOCAL = new ThreadLocal<>();

    /**
     * 初始化request  response
     *
     * @param request
     * @param response
     */
    @ModelAttribute
    public void initHttpServlet(HttpServletRequest request, HttpServletResponse response) {
        REQUESTTHREADLOCAL.set(request);
        RESPONSETHREADLOCAL.set(response);
    }

    /**
     * 获取线程安全的request
     *
     * @return
     */
    public HttpServletRequest request() {
        return (HttpServletRequest) REQUESTTHREADLOCAL.get();
    }

    /**
     * 获取线程安全的response
     *
     * @return
     */
    public HttpServletResponse response() {
        return (HttpServletResponse) RESPONSETHREADLOCAL.get();
    }

    /**
     * 获取用户token
     *
     * @return
     */
    public String getUserToken() {
        String token = request().getHeader("Authorization");
        log.info("--getUserToken--" + token);
        if (StringUtils.isEmpty(token)) {
            return null;
        } else {
            return token;
        }
    }

    /**
     * 获取userId
     *
     * @return
     */
    public String getUserId() {
        return TokenUtil.getUserId(getUserToken());
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public String getUserName() {
        return TokenUtil.getUserName(getUserToken());
    }

}
