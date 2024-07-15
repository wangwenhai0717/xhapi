package com.xunhao.xhapiclientsdk;

import com.xunhao.xhapiclientsdk.client.XhapiClient;
import com.xunhao.xhapiclientsdk.strategy.BaseContext;
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
    public BaseContext xhapiClient() {
        XhapiClient xhapiClient = new XhapiClient(accessKey, secretKey);
        BaseContext baseContext = new BaseContext();
        baseContext.setApiClient(xhapiClient);
        return baseContext;
    }
}
