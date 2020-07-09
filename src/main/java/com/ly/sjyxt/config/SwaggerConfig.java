package com.ly.sjyxt.config;
import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * @author 鲁先生
 * @create 2020-06-07 23:39
 **/
@Configuration
@EnableSwagger2 //开启swaager
@EnableSwaggerBootstrapUI
public class SwaggerConfig implements WebMvcConfigurer {
    //http://localhost:8080/swagger-ui.html
    //http://localhost:9895/swagger-ui.html
    //配置类swagger 的docket的bean 实例
    @Bean
    public Docket docket(Environment environment) {

        //获取项目中的生产环境
        Profiles profiles = Profiles.of("dev", "test");
        //获取项目的环境
        //通过environment.acceptsProfiles 判断是否处在自己设定的环境中。
        // 如果enable 为false，则swagger不能浏览器中访问。
        boolean hjbl = environment.acceptsProfiles(profiles);
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("鲁先生")
                .enable(hjbl)
                .select()
                // requestHandlerSwlwctors 配置要扫描接口的方式。
                //basePackage 指定要扫描的包
                // any() 扫描全部
                // none() 不扫描
                //withClassAnnotaction 扫描类上的注解。
                .apis(RequestHandlerSelectors.basePackage("com.ly.sjyxt.api"))
                .apis(RequestHandlerSelectors.any())
                //.paths(PathSelectors.any())
                .build();
    }

    //配置swagger 信息=apiinfo
    public ApiInfo apiInfo() {
        //作者信息                          sjyxt
        Contact contact = new Contact("鲁先生", "http://localhost:9092/sjyxt/", "21312");
        return new ApiInfo(
                "鲁先生的 数据源系统  swagger Api 文档",
                "des",
                "v1.0",
                "http://localhost:9092/sjyxt/",
                contact,
                "apache 2.0",
                "http://localhost:9092/sjyxt/",
                new ArrayList()
        );
    }

    @Bean
    public Docket docket1(){
        return  new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(setInfo())
                .groupName("a")
                .select()
                .apis(RequestHandlerSelectors.any())
                .build();
    }

    private ApiInfo setInfo() {
        Contact c1 = new Contact("a先生","baidu.com","110@.com");
        return new ApiInfo(
                "A先生的API 文档",
                "dev",
                "v1.0",
                "www.com",
                c1,
                "api 2.9",
                "bilibili",
                new ArrayList()
        );
    }

    @Bean
    public Docket docket2(){
        return  new Docket(DocumentationType.SWAGGER_2).groupName("b");
    }
    @Bean
    public Docket docket3(){
        return  new Docket(DocumentationType.SWAGGER_2).groupName("c");
    }


}
