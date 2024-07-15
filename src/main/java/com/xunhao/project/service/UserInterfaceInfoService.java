package com.xunhao.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.xunhao.xhapicommon.model.entity.UserInterfaceInfo;

public interface UserInterfaceInfoService extends IService<UserInterfaceInfo>{

    void validInterfaceInfo(UserInterfaceInfo info, boolean add);

    /**
     * 调用接口统计
     */
    boolean invokeCount(long interfaceInfoId, long userId);

    /**
     * 添加用户调用接口记录
     * @param userId 用户id
     * @param interfaceInfoId 接口id
     * @return
     */
    boolean addUserInvokeInterface(long userId,long interfaceInfoId);
}
