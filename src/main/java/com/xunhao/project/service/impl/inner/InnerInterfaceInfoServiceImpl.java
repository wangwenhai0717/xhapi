package com.xunhao.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xunhao.project.common.ErrorCode;
import com.xunhao.project.exception.BusinessException;
import com.xunhao.project.mapper.InterfaceInfoMapper;
import com.xunhao.xhapicommon.model.entity.InterfaceInfo;
import com.xunhao.xhapicommon.service.InnerInterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public InterfaceInfo getInterfaceInfo(String url, String method) {
        //校验参数
        if (StringUtils.isAnyBlank(url, method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //创建条件查询构造器
        QueryWrapper<InterfaceInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("url", url);
        wrapper.eq("method", method);
        return interfaceInfoMapper.selectOne(wrapper);
    }
}
