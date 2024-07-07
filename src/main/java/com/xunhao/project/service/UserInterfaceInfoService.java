package com.xunhao.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xunhao.xhapicommon.model.entity.UserInterfaceInfo;

public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    void validInterfaceInfo(UserInterfaceInfo info, boolean add);

    /**
     * 调用接口统计
     */
    boolean invokeCount(long interfaceInfoId, long userId);
}
