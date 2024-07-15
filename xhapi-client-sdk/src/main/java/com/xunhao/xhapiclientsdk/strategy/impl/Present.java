package com.xunhao.xhapiclientsdk.strategy.impl;

import com.xunhao.xhapiclientsdk.client.XhapiClient;
import com.xunhao.xhapiclientsdk.strategy.BaseStrategy;

/**
 * 占位类
 *
 * @author xh
 * @version 1.0
 */
public class Present implements BaseStrategy {
    @Override
    public String handlerRequest(String url, String params, XhapiClient xhapiClient) {
        return xhapiClient.definitionRequest(url, " ");
    }
}
