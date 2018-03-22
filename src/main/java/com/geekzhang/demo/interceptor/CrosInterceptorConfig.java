package com.geekzhang.demo.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@Slf4j
public class CrosInterceptorConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private CrsoInterceptor crsoInterceptor;
    @Value("${web.exclude.path}")
    private String excludePath;

    /**
     * 添加拦截器
     *
     * @param registry
     */

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("拦截器配置文件,过滤路径：[{}]", excludePath);
        registry.addInterceptor(crsoInterceptor).addPathPatterns("/**")
                .excludePathPatterns(excludePath.split(","));
    }
}
