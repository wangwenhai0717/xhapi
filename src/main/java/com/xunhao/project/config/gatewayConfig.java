package com.xunhao.project.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "xh.gateway")
@Data
public class gatewayConfig {

    private String host;

}
