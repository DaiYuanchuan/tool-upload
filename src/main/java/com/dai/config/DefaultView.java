package com.dai.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @description: 配置默认视图
 * @author: Dai Yuanchuan
 * @create: 2019-03-19 19:22
 **/
@Configuration
public class DefaultView extends WebMvcConfigurationSupport {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 静态文件映射
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}

