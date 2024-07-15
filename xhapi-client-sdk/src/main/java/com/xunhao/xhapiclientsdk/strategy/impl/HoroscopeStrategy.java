package com.xunhao.xhapiclientsdk.strategy.impl;

import com.google.gson.Gson;
import com.xunhao.xhapiclientsdk.client.XhapiClient;
import com.xunhao.xhapiclientsdk.constant.ResponseResult;
import com.xunhao.xhapiclientsdk.entity.params.HoroscopeParams;
import com.xunhao.xhapiclientsdk.strategy.BaseStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * Post请求星座接口
 * @author xh
 * @version 1.0
 */
@Slf4j
public class HoroscopeStrategy implements BaseStrategy {
    @Override
    public String handlerRequest(String url, String params, XhapiClient xhapiClient) {
        log.info("url = {} , params = {}", url, params);
        Gson gson = new Gson();
        HoroscopeParams horoscopeParams = null;
        if (StringUtils.isBlank(params)){
            return ResponseResult.BASE_RESULT;
        }
        try {
            horoscopeParams = gson.fromJson(params, HoroscopeParams.class);
        } catch (Exception e) {
            log.error("转换json出现异常,错误为: {},输入参数为: {}", e.getMessage(),params);
            return ResponseResult.BASE_RESULT;
        }
        return xhapiClient.definitionRequest(url, horoscopeParams);
    }
}
