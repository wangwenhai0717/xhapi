package com.xunhao.xunhao;

import com.xunhao.xhapiclientsdk.client.XhapiClient;

import com.xunhao.xhapiclientsdk.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class XunhaoApplicationTests {

    @Resource
    private XhapiClient xhapiClient;

    @Test
    void contextLoads() {
        Object xunhao = xhapiClient.getNameByGet("xunhao");
        User user = new User();
        user.setName("xh");
        Object userNameByPost = xhapiClient.getUserNameByPost(user);
        System.out.println(xunhao);
        System.out.println(userNameByPost);

    }

}
