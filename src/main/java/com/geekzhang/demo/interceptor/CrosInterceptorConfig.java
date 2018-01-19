package com.geekzhang.demo.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class CrosInterceptorConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private CrsoInterceptor crsoInterceptor;
    @Value("${music.exclude.path}")
    private String excludePath;

    /**
     * 添加拦截器
     *
     * @param registry
     */

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(crsoInterceptor).addPathPatterns("/**")
                .excludePathPatterns(excludePath.split(","));
    }
}
