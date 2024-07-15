package com.xunhao.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xunhao.project.common.ErrorCode;
import com.xunhao.project.exception.BusinessException;
import com.xunhao.project.service.UserInterfaceInfoService;
import com.xunhao.project.mapper.UserInterfaceInfoMapper;
import com.xunhao.xhapicommon.model.entity.UserInterfaceInfo;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo> implements UserInterfaceInfoService{
    @Override
    public void validInterfaceInfo(UserInterfaceInfo info, boolean add) {
        if (info == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long interfaceInfoId = info.getInterfaceInfoId();
        Long userId = info.getUserId();
        Integer leftNum = info.getLeftNum();
        // 调用接口时，参数不能为空
        if (add) {
            if (interfaceInfoId <= 0 || userId <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"接口或用户不存在");
            }
        }
        // 有参数则校验
        if (leftNum <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求错误");
        }

    }

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        //判断
        if (interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("interface_info_id", interfaceInfoId);
        updateWrapper.eq("user_id", userId);
        updateWrapper.gt("left_num", 0);
        //满足条件剩余次数减一，总调用次数加一
        updateWrapper.setSql("left_num = left_num - 1, total_num = total_num + 1");
        return this.update(updateWrapper);
    }

    @Override
    public boolean addUserInvokeInterface(long userId, long interfaceInfoId) {
        //判断
        if (interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口或用户不存在");
        }
        QueryWrapper<UserInterfaceInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("interface_info_id", interfaceInfoId);
        wrapper.eq("user_id", userId);
        UserInterfaceInfo user = this.getOne(wrapper);
        if (user != null) {
            return true;
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        userInterfaceInfo.setUserId(userId);
        userInterfaceInfo.setInterfaceInfoId(interfaceInfoId);
        boolean save = save(userInterfaceInfo);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"添加失败");
        }
        return save;
    }
}




