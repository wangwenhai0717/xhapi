package com.xunhao.xhapiclientsdk;

import com.xunhao.xhapiclientsdk.client.XhapiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("xhapi.client")
@Data
@ComponentScan
public class XhapiClientConfig {

    private String accessKey;
    private String secretKey;

    @Bean
    public XhapiClient xhapiClient() {
        return new XhapiClient(accessKey, secretKey);
    }
}
