package com.xunhao.xhapiclientsdk.client;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.xunhao.xhapiclientsdk.utils.SignUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
public class XhapiClient {

    private String accessKey;
    private String secretKey;

    public XhapiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    private static String GATEWAY_HOST = "http://localhost:8090";

    public void setGatewayHost(String gatewayHost) {
        GATEWAY_HOST = gatewayHost;
    }

    private Map<String,String> getHeaderMap(String body, String method) throws UnsupportedEncodingException{
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
        hashMap.put("method", method);
        return hashMap;
    }

    public String invokeInterface(String params, String url, String method) throws UnsupportedEncodingException{
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST + url)
                .header("Accept-Charset", CharsetUtil.UTF_8)
                .addHeaders(getHeaderMap(params, method))
                .body(params)
                .execute();
        return JSONUtil.formatJsonStr(httpResponse.body());
    }
}
