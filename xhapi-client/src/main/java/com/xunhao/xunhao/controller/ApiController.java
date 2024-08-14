package com.xunhao.xunhao.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.xunhao.xhapiclientsdk.entity.User;
import org.springframework.web.bind.annotation.*;

/**
 * 功能接口
 */

@RestController
@RequestMapping(value = "/xhapi")
public class ApiController {

    @PostMapping("/name")
    public String getUserNameByPost(@RequestBody User user) {
        return "你的用户名是" + user.getName();
    }

    @PostMapping("/rand.avatar")
    public String randAvatar() {
        HttpResponse response = HttpRequest
                .get("https://api.uomg.com/api/rand.avatar?sort=男&format=json")
                .execute();
        return response.body();
    }

    @PostMapping("/getMoYu")
    public String getMoYu() {
        HttpResponse response = HttpRequest
                .get("https://api.vvhan.com/api/moyu?type=json")
                .execute();
        return response.body();
    }

    @PostMapping("/api.php")
    public String randImages() {
        HttpResponse response = HttpRequest
                .get("http://api.btstu.cn/sjbz/api.php?lx=dongman&format=json")
                .execute();
        return response.body();
    }

    @PostMapping("/xh/api.php")
    public String poisonChicken() {
        HttpResponse response = HttpRequest
                .get("https://api.btstu.cn/qqxt/api.php?qq=2944797539")
                .execute();
        return response.body();
    }


    @PostMapping("/long2dwz")
    public String long2dwz() {
        HttpResponse response = HttpRequest
                .get("https://api.uomg.com/api/get.qqdj?qq=2944797539&skey=@surhcc2")
                .execute();
        return response.body();
    }

}
