package com.xunhao.xhapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.xunhao.xhapiclientsdk.entity.User;
import com.xunhao.xhapiclientsdk.utils.SignUtil;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class XhapiClient {

    private static final String GATEWAY_HOST = "http://106.54.193.109:8090";

    private String accessKey;
    private String secretKey;

    public XhapiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getNameByGet(String name) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        return HttpUtil.get(GATEWAY_HOST + "/api/name/",paramMap);
    }

    public String getNameByPost(String name) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        return HttpUtil.post(GATEWAY_HOST + "/api/name/",paramMap);
    }

    public String getUserNameByPost(User user) {
        String json = JSONUtil.toJsonStr(user);
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + "/api/name/user")
                .charset(StandardCharsets.UTF_8)
                .body(json)
                .addHeaders(getHeaderMap(json))
                .execute();
        String result = httpResponse.body();
        return result;
    }

    private Map<String,String> getHeaderMap(String body) {
        Map<String,String> hashMap = new HashMap<>();
        hashMap.put("accessKey", accessKey);
        //不能直接发送
//        hashMap.put("secretKey", secretKey);
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        hashMap.put("body", body);
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis()/1000));
        hashMap.put("sign", SignUtil.getSign(body,secretKey));
        return hashMap;
    }
}
