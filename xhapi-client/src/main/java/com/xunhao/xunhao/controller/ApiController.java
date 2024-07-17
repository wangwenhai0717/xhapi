package com.xunhao.xunhao.controller;

import com.xunhao.xhapiclientsdk.entity.User;
import com.xunhao.xhapiclientsdk.entity.params.HoroscopeParams;
import com.xunhao.xunhao.service.XhInterfaceApiService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 功能接口
 */

@RestController
@RequestMapping(value = "/xhapi", produces = "application/json;charset=utf-8")
public class ApiController {

    @Resource
    private XhInterfaceApiService xhInterfaceApiService;

    @PostMapping("/name")
    public String getUserNameByPost(@RequestBody User user) {
        String result = "用户的名字是" + user.getName();
        return result;
    }

    @PostMapping("/RandomScenery")
    public String RandomScenery() {
        return xhInterfaceApiService.getRandomScenery();
    }

    @PostMapping("/getMoYu")
    public String getMoYu() {
        return xhInterfaceApiService.getMoYu();
    }

    @PostMapping("/getRandomWallpaper")
    public String getRandomWallpaper() {
        return xhInterfaceApiService.getRandomWallpaper();
    }

    @PostMapping("/getHoroscope")
    public String getHoroscope(@RequestBody HoroscopeParams horoscopeParams) {
        return xhInterfaceApiService.getHoroscope(horoscopeParams);
    }
}
