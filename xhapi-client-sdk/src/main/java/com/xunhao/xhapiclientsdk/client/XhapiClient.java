package com.xunhao.xhapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.xunhao.xhapiclientsdk.utils.SignUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Data
public class XhapiClient {

    private static final String GATEWAY_HOST = "http://106.54.193.109:8090";

    private String accessKey;
    private String secretKey;

    public XhapiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    private <T> String getJson(T arg) {
        return JSONUtil.toJsonStr(arg);
    }

    private Map<String,String> getHeaderMap(String body) {
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("accessKey", accessKey);
        //不能直接发送
//        hashMap.put("secretKey", secretKey);
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        try {
            hashMap.put("body", URLEncoder.encode(body, "utf8"));
        } catch (Exception e) {
            log.error("加密传递参数出错,异常信息为: " + e);
        }
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis()/1000));
        hashMap.put("sign", SignUtil.getSign(body,secretKey));
        return hashMap;
    }

    public <T> String definitionRequest(String url, T arg) {
        String json = getJson(arg);
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + url)
                .charset(StandardCharsets.UTF_8)
                .body(json)
                .addHeaders(getHeaderMap(json))
                .execute();
        String result = httpResponse.body();
        log.info("SDK 返回状态为: {}", httpResponse.getStatus());
        log.info("SDK 返回结果为: {}", result);
        return result;
    }
}
