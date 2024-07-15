package com.xunhao.xhapiclientsdk.strategy;

import com.xunhao.xhapiclientsdk.client.XhapiClient;

/**
 * 抽出公有方法
 * @author xh
 * @version 1.0
 */
public interface BaseStrategy {

    String handlerRequest(String url, String params, XhapiClient xhapiClient);
}
