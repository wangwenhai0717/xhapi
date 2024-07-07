package com.xunhao.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xunhao.project.common.ErrorCode;
import com.xunhao.project.exception.BusinessException;
import com.xunhao.project.exception.ThrowUtils;
import com.xunhao.project.mapper.InterfaceInfoMapper;
import com.xunhao.project.service.InterfaceInfoService;
import com.xunhao.xhapicommon.model.entity.InterfaceInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo> implements InterfaceInfoService {
    @Override
    public void validInterfaceInfo(InterfaceInfo info, boolean add) {
        if (info == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = info.getName();
        String description = info.getDescription();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(name, description), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }
        if (StringUtils.isNotBlank(description) && description.length() > 100) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "描述过长");
        }
    }
}




