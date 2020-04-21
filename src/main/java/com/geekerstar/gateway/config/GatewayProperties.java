package com.geekerstar.gateway.config;

import com.google.common.collect.Maps;
import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

/**
 * @author geekerstar
 * @date 2020/4/16 10:04
 * @description
 */
@Data
@SpringBootConfiguration
@PropertySource(value = {"classpath:gateway.properties"})
@ConfigurationProperties(prefix = "gateway")
public class GatewayProperties {

    private Map<String, String> server = Maps.newHashMapWithExpectedSize(3);

}
