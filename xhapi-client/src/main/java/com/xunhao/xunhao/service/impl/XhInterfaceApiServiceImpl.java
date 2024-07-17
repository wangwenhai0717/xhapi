package com.xunhao.xunhao.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xunhao.xhapiclientsdk.entity.params.HoroscopeParams;
import com.xunhao.xunhao.service.XhInterfaceApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class XhInterfaceApiServiceImpl implements XhInterfaceApiService {

    @Override
    public String getRandomScenery() {
        HttpResponse response = HttpRequest
                .get("https://api.vvhan.com/api/wallpaper/views?type=json")
                .execute();
        return response.body();
    }

    @Override
    public String getMoYu() {
        HttpResponse response = HttpRequest
                .get("https://api.vvhan.com/api/moyu?type=json")
                .execute();
        return response.body();
    }

    @Override
    public String getRandomWallpaper() {
        HttpResponse response = HttpRequest
                .get("https://api.vvhan.com/api/bing?rand=sj")
                .execute();
        String html = response.body();
        log.info("获取随机壁纸,原始返回结果为: {}", html);
        // 使用正则表达式匹配href属性的值
        Pattern pattern = Pattern.compile("<a\\s+href\\s*=\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(html);

        // 如果未找到匹配的内容，直接返回原始结果,无法预览
        if (!matcher.find()) {
            return html;
        }
        String href = matcher.group(1);
        return "{\"url\":\"" + href + "\"}";
    }

    @Override
    public String getHoroscope(HoroscopeParams horoscopeParams) {
        if (horoscopeParams == null || StringUtils.isBlank(horoscopeParams.getHoroscope())) {
            horoscopeParams = new HoroscopeParams();
        }
        String horoscope = horoscopeParams.getHoroscope();
        HttpResponse response = HttpRequest
                .get("https://api.vvhan.com/api/horoscope?type=" + horoscope + "&time=today")
                .execute();
        return response.body();
    }

}
