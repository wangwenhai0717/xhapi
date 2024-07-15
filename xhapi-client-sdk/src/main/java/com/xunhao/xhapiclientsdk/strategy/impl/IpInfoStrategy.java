package com.xunhao.xhapiclientsdk.strategy.impl;

import com.google.gson.Gson;
import com.xunhao.xhapiclientsdk.client.XhapiClient;
import com.xunhao.xhapiclientsdk.constant.ResponseResult;
import com.xunhao.xhapiclientsdk.entity.params.IpInfoParams;
import com.xunhao.xhapiclientsdk.strategy.BaseStrategy;
import lombok.extern.slf4j.Slf4j;

/**
 * Post请求获取IP信息接口
 * @author xh
 * @version 1.0
 */
@Slf4j
public class IpInfoStrategy implements BaseStrategy {

    @Override
    public String handlerRequest(String url, String params, XhapiClient xhapiClient) {
        log.info("url = {} , params = {}", url, params);
        Gson gson = new Gson();
        IpInfoParams ipInfoParams = null;
        try {
            ipInfoParams = gson.fromJson(params, IpInfoParams.class);
        } catch (Exception e) {
            log.error("转换json出现异常,错误为: {},输入参数为: {}", e.getMessage(), params);
            return ResponseResult.BASE_RESULT;
        }
        if (ipInfoParams == null) {
            ipInfoParams = new IpInfoParams();
        }
        return xhapiClient.definitionRequest(url, ipInfoParams);
    }
}
