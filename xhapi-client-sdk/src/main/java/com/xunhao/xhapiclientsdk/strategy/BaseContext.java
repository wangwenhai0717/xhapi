package com.xunhao.xhapiclientsdk.strategy;

import com.xunhao.xhapiclientsdk.client.XhapiClient;
import com.xunhao.xhapiclientsdk.constant.MyUrl;
import com.xunhao.xhapiclientsdk.strategy.impl.HoroscopeStrategy;
import com.xunhao.xhapiclientsdk.strategy.impl.Present;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 策略核心上下文类
 * @author xh
 * @version 1.0
 */
public class BaseContext {
    private static final Map<String, BaseStrategy> strategyMap = new ConcurrentHashMap<>();
    private XhapiClient xhapiClient;
    private static final Present PRESENT = new Present();

    static {
        strategyMap.put(MyUrl.MO_YU, PRESENT);
        strategyMap.put(MyUrl.HOROSCOPE, new HoroscopeStrategy());
        strategyMap.put(MyUrl.RANDOM_SCENERY, PRESENT);
        strategyMap.put(MyUrl.UserName, PRESENT);
        strategyMap.put(MyUrl.RANDOM_WALLPAPER, PRESENT);
    }

    public String handler(String url, String params) {
        BaseStrategy baseStrategy = strategyMap.get(url);
        return baseStrategy.handlerRequest(url, params, xhapiClient);
    }

    public void setApiClient(XhapiClient xhapiClient) {
        this.xhapiClient = xhapiClient;
    }
}
