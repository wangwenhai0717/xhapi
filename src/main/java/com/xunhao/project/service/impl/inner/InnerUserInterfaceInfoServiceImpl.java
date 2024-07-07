package com.xunhao.project.service.impl.inner;

import com.xunhao.project.service.UserInterfaceInfoService;
import com.xunhao.xhapicommon.service.InnerUserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService interfaceInfoService;

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        return interfaceInfoService.invokeCount(interfaceInfoId, userId);
    }
}
