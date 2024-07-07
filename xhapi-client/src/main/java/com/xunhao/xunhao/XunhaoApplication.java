package com.xunhao.xunhao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class XunhaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(XunhaoApplication.class, args);
    }

}
