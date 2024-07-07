package com.xunhao.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xunhao.project.annotation.AuthCheck;
import com.xunhao.project.common.BaseResponse;
import com.xunhao.project.common.ErrorCode;
import com.xunhao.project.common.ResultUtils;
import com.xunhao.project.constant.UserConstant;
import com.xunhao.project.exception.BusinessException;
import com.xunhao.project.mapper.UserInterfaceInfoMapper;
import com.xunhao.project.model.vo.InterfaceInfoVo;
import com.xunhao.project.service.InterfaceInfoService;
import com.xunhao.xhapicommon.model.entity.InterfaceInfo;
import com.xunhao.xhapicommon.model.entity.UserInterfaceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分析控制器
 */
@RestController
@RequestMapping("/analysis")
@Slf4j
public class AnalysisController {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @GetMapping("/top/interface/invoke")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<InterfaceInfoVo>> listTopInvokeInterfaceInfo() {
        //查询调用次数最多的接口信息列表
        List<UserInterfaceInfo> list = userInterfaceInfoMapper.listTopInvokeInterfaceInfo(5);
        //将接口信息按照接口ID分组，便于关联查询
        Map<Long, List<UserInterfaceInfo>> map = list.stream()
                .collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));
        //创建查询接口信息的条件构造器
        QueryWrapper<InterfaceInfo> wrapper = new QueryWrapper<>();
        //设置查询条件，使用接口信息ID在接口信息映射中的键集合进行条件匹配
        wrapper.in("id", map.keySet());
        //调用接口信息群服务的list方法，传入条件包装器，获取符合条件的接口信息列表
        List<InterfaceInfo> info = interfaceInfoService.list(wrapper);
        //判断查询是否为空
        if (CollectionUtils.isEmpty(info)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        //构建接口信息Vo列表，使用流式处理将接口信息映射为接口信息Vo对象，并加入列表中
        List<InterfaceInfoVo> listVo = info.stream().map(interfaceInfo -> {
            //创建一个新的接口信息对象Vo
            InterfaceInfoVo vo = new InterfaceInfoVo();
            //将接口信息复制到vo中
            BeanUtils.copyProperties(interfaceInfo, vo);
            //从接口信息ID对应的映射中获取调用次数
            int totalNum = map.get(interfaceInfo.getId()).get(0).getTotalNum();
            //将调用次数设置到接口信息Vo对象中
            vo.setTotalNum(totalNum);
            //返回构建好的接口信息Vo对象
            return vo;
        }).collect(Collectors.toList());
        //返回处理结果
        return ResultUtils.success(listVo);
    }
}
