package com.xunhao.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xunhao.xhapicommon.model.entity.UserInterfaceInfo;

import java.util.List;

public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);
}




