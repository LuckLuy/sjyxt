package com.ly.sjyxt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @ClassName: MyrConfigurationCos
 * @Description: 服务跨域访问配置
 */

@Configuration
public class MyConfigurationCros {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")//允许进行跨域访问的路径
                        .allowedOrigins("*")//允许进行跨域的主机ip
                        //.allowedMethods("GET","OPTIONS","POST")//允许的访问方法
                        .allowedMethods("*")//允许的访问方法
                        .allowedHeaders("*")//默认所有的header,自然包括了自定义的
                        // .exposedHeaders("header1", "header2")//排除有关的header
                        .allowCredentials(false).maxAge(3600);//若要返回cookie、携带seesion等信息则将此项设置true
            }
        };
    }
}
