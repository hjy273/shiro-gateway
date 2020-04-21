package com.geekerstar.gateway.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static com.geekerstar.gateway.constant.CommonConstant.GATEWAY;
import static com.geekerstar.gateway.constant.CommonConstant.SWAGGER_SUFFIX;

/**
 * @author geekerstar
 * @date 2020/4/16 11:30
 * @description 网关启动器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GatewayStartedUpRunner implements ApplicationRunner {

    @Value("${server.port:8080}")
    private String port;

    private final ConfigurableApplicationContext context;

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void run(ApplicationArguments args) throws UnknownHostException {
        try {
            stringRedisTemplate.hasKey(GATEWAY);
            log.info("++++++++++++++++++++++++++++++++++++++++++++");
            log.info("【权限配置】加载成功!");
            log.info("【Redis】连接成功!");
        } catch (Exception e) {
            log.error("+++++++++++++++++++++++++++++++++++++++++++++++++++");
            log.error("【统一认证授权网关】启动失败，{}", e.getMessage());
            log.error("【Redis】连接异常，请检查Redis连接配置并确保Redis服务已启动");
            log.error("+++++++++++++++++++++++++++++++++++++++++++++++++++");
            context.close();
        }
        if (context.isActive()) {
            InetAddress address = InetAddress.getLocalHost();
            String url = String.format("http://%s:%s%s", address.getHostAddress(), port, SWAGGER_SUFFIX);
            log.info("【统一认证授权网关】启动成功!");
            log.info("【Swagger】{}", url);
            log.info("++++++++++++++++++++++++++++++++++++++++++++");
        }
    }
}
