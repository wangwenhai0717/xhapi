package com.xunhao.xunhao.service;

import com.xunhao.xhapiclientsdk.entity.params.HoroscopeParams;

public interface XhInterfaceApiService {

    /**
     * 获取随机风景图片
     * @return
     */
    String getRandomScenery();

    /**
     * 获取摸鱼日历
     * @return
     */
    String getMoYu();

    /**
     * 获取随机壁纸
     */
    String getRandomWallpaper();

    /**
     * 获取星座运势
     * @param horoscopeParams
     * @return
     */
    String getHoroscope(HoroscopeParams horoscopeParams);

}
