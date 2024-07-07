package com.xunhao.xhapicommon.service;

public interface InnerUserInterfaceInfoService {

    /**
     * 调用接口统计
     */
    boolean invokeCount(long interfaceInfoId, long userId);
}
