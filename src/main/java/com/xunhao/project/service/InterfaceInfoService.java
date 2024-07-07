package com.xunhao.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xunhao.xhapicommon.model.entity.InterfaceInfo;

public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo info, boolean add);
}
