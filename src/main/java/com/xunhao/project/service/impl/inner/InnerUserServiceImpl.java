package com.xunhao.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xunhao.project.common.ErrorCode;
import com.xunhao.project.exception.BusinessException;
import com.xunhao.project.mapper.UserMapper;
import com.xunhao.xhapicommon.model.entity.User;
import com.xunhao.xhapicommon.service.InnerUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User getInvokeUser(String accessKey) {
        //参数校验
        if (StringUtils.isAnyBlank(accessKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //创建查询条件构造器
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accessKey", accessKey);
        User user = userMapper.selectOne(queryWrapper);
        return user;
    }

    @Override
    public boolean getLeftNum(long userId) {
        if (userId <= 0){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }
        User user = userMapper.selectById(userId);
        if (user.getLeftNum() <= 0) {
            return false;
        }
        return true;
    }


}
