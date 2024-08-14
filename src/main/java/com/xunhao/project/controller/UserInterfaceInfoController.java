package com.xunhao.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xunhao.project.annotation.AuthCheck;
import com.xunhao.project.common.*;
import com.xunhao.project.constant.CommonConstant;
import com.xunhao.project.constant.UserConstant;
import com.xunhao.project.exception.BusinessException;
import com.xunhao.project.exception.ThrowUtils;
import com.xunhao.project.model.dto.user.UserSignUpdateRequest;
import com.xunhao.project.model.dto.userInterfaceInfo.UserInterfaceInfoAddRequest;
import com.xunhao.project.model.dto.userInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.xunhao.project.model.dto.userInterfaceInfo.UserInterfaceInfoUpdateRequest;
import com.xunhao.project.service.UserInterfaceInfoService;
import com.xunhao.project.service.UserService;
import com.xunhao.xhapicommon.model.entity.User;
import com.xunhao.xhapicommon.model.entity.UserInterfaceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class UserInterfaceInfoController {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建
     *
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addInterface(@RequestBody UserInterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo info = new UserInterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, info);
        userInterfaceInfoService.validInterfaceInfo(info, true);
        User loginUser = userService.getLoginUser(request);
        info.setUserId(loginUser.getId());
        boolean result = userInterfaceInfoService.save(info);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newPostId = info.getId();
        return ResultUtils.success(newPostId);
    }

    /**
     * 删除
     *
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteInterface(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = deleteRequest.getId();
        // 判断是否存在
        UserInterfaceInfo oldInfo = userInterfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInfo == null, ErrorCode.NOT_FOUND_ERROR);
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = userInterfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param interfaceInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateInterface(@RequestBody UserInterfaceInfoUpdateRequest interfaceInfoUpdateRequest) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo info = new UserInterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, info);
        // 参数校验
        userInterfaceInfoService.validInterfaceInfo(info, false);
        long id = interfaceInfoUpdateRequest.getId();
        // 判断是否存在
        UserInterfaceInfo oldInfo = userInterfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInfo == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = userInterfaceInfoService.updateById(info);
        return ResultUtils.success(result);
    }

    /**
     * 分页获取列表（仅管理员）
     *
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    @GetMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserInterfaceInfo>> listInterfaceInfoByPage(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        if (userInterfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
            UserInterfaceInfo interfaceInfoQuery = new UserInterfaceInfo();
            BeanUtils.copyProperties(userInterfaceInfoQueryRequest, interfaceInfoQuery);
            long current = userInterfaceInfoQueryRequest.getCurrent();
            long size = userInterfaceInfoQueryRequest.getPageSize();
            String sortField = userInterfaceInfoQueryRequest.getSortField();
            String sortOrder = userInterfaceInfoQueryRequest.getSortOrder();

            if (size > 50) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
            queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                    sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
            Page<UserInterfaceInfo> interfaceInfoPage = userInterfaceInfoService.page(new Page<>(current, size), queryWrapper);
            return ResultUtils.success(interfaceInfoPage);
        }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<UserInterfaceInfo> getInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo info = userInterfaceInfoService.getById(id);
        if (info == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(info);
    }

    /**
     * 签到功能
     */
    @PostMapping("/sign")
    public BaseResponse<Boolean> UserSign(@RequestBody UserSignUpdateRequest signUpdateRequest) {
        if (signUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(signUpdateRequest.getId());
        if (user.getLeftNum() == 100) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "积分已上限");
        }
        Date oldTime = user.getSignTime();
        Date nowTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat compare = new SimpleDateFormat("yyyy-MM-dd");
        String currentTime = compare.format(nowTime);
        String oldDateTime = compare.format(oldTime);
        if (currentTime.equals(oldDateTime)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "今日已签到");
        }
        user.setLeftNum(user.getLeftNum() + 1);
        Date parse = null;
        try {
            String format = dateFormat.format(nowTime);
            parse = dateFormat.parse(format);
        } catch (ParseException e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "签到时间转换异常");
        }
        user.setSignTime(parse);
        userService.updateById(user);
        return ResultUtils.success(true);
    }
}