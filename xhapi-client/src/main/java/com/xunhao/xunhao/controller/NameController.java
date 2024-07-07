package com.xunhao.xunhao.controller;

import com.xunhao.xhapiclientsdk.entity.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 名称接口
 */

@RestController
@RequestMapping("/name")
public class NameController {

    @GetMapping("/get")
    public String getNameByGet(String name) {
        return "GET 你的名字是" +name;
    }

    @PostMapping("/post")
    public String getNameByPost(@RequestParam String name) {
        return "POST 你的名字是" +name;
    }

    @PostMapping("/user")
    public String getUserNameByPost(@RequestBody User user, HttpServletRequest request) {
//        String accessKey = request.getHeader("accessKey");
//        String nonce = request.getHeader("nonce");
//        String timestamp = request.getHeader("timestamp");
//        String sign = request.getHeader("sign");
//        String body = request.getHeader("body");
//        //先到数据库查是否已分配给用户
//        if (!accessKey.equals("xunhao")) {
//            throw new RuntimeException("无权限");
//        }
//        if (Long.parseLong(nonce) > 10000) {
//            throw new RuntimeException("无权限");
//        }
//        //当前时间不超过5分钟
//        long time = System.currentTimeMillis();
////        if (Long.parseLong(nonce) > 10000) {
////            throw new RuntimeException("无权限");
////        }
//        //要从数据库去查secretKey
//        String serverSign = SignUtil.getSign(body, "qwertyui");
//        if (!sign.equals(serverSign)) {
//            throw new RuntimeException("无权限");
//        }
        String result = "用户的名字是" + user.getName();
        return result;
    }
}
