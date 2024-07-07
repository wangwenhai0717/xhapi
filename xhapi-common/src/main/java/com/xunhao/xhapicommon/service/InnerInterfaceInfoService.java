package com.xunhao.xhapicommon.service;

import com.xunhao.xhapicommon.model.entity.InterfaceInfo;

public interface InnerInterfaceInfoService {

    /**
     * 查询模拟接口是否存在
     */
    InterfaceInfo getInterfaceInfo(String url, String method);
}
