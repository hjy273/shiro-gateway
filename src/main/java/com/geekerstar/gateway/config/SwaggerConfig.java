package com.geekerstar.gateway.config;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

/**
 * @author geekerstar
 * @date 2020/4/13 09:36
 * @description Swagger配置类
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket allApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("1000.所有接口")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.geekerstar.gateway.controller"))
                .paths(PathSelectors.any())
                .build()
                .securityContexts(Lists.newArrayList(securityContext()))
                .securitySchemes(Lists.<SecurityScheme>newArrayList(token(), appId()))
                .apiInfo(apiInfo());
    }

    private ApiKey token() {
        return new ApiKey("Token", "authorization", "header");
    }

    private ApiKey appId() {
        return new ApiKey("AppId", "appId", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/.*"))
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(new SecurityReference("BearerToken", authorizationScopes));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("统一认证授权网关")
                .description("统一认证授权网关：集成权限管理及网关路由功能")
                .version("1.0")
                .termsOfServiceUrl("")
                .contact(new Contact("Geekerstar", "https://www.geekerstar.com", ""))
                .license("")
                .licenseUrl("")
                .build();
    }
}
