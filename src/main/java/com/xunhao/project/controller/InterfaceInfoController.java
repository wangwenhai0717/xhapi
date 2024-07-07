package com.xunhao.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.xunhao.project.annotation.AuthCheck;
import com.xunhao.project.common.*;
import com.xunhao.project.constant.CommonConstant;
import com.xunhao.project.constant.UserConstant;
import com.xunhao.project.exception.BusinessException;
import com.xunhao.project.exception.ThrowUtils;
import com.xunhao.project.model.dto.interfaceInfo.InterfaceInfoAddRequest;
import com.xunhao.project.model.dto.interfaceInfo.InterfaceInfoInvokeRequest;
import com.xunhao.project.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.xunhao.project.model.dto.interfaceInfo.InterfaceInfoUpdateRequest;
import com.xunhao.project.model.enums.InterfaceInfoEnum;
import com.xunhao.project.service.InterfaceInfoService;
import com.xunhao.project.service.UserService;
import com.xunhao.xhapiclientsdk.client.XhapiClient;
import com.xunhao.xhapicommon.model.entity.InterfaceInfo;
import com.xunhao.xhapicommon.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/interface")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private XhapiClient xhapiClient;

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
    public BaseResponse<Long> add(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo info = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, info);
        interfaceInfoService.validInterfaceInfo(info, true);
        User loginUser = userService.getLoginUser(request);
        info.setUserId(loginUser.getUserName());
        boolean result = interfaceInfoService.save(info);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newPostId = info.getId();
        return ResultUtils.success(newPostId);
    }

    /**
     * 删除
     *
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> delete(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInfo == null, ErrorCode.NOT_FOUND_ERROR);
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = interfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param interfaceInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> update(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo info = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, info);
        // 参数校验
        interfaceInfoService.validInterfaceInfo(info, false);
        long id = interfaceInfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInfo == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = interfaceInfoService.updateById(info);
        return ResultUtils.success(result);
    }

    /**
     * 分页获取列表
     *
     * @param interfaceInfoQueryRequest
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
            InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
            BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
            long current = interfaceInfoQueryRequest.getCurrent();
            long size = interfaceInfoQueryRequest.getPageSize();
            String sortField = interfaceInfoQueryRequest.getSortField();
            String sortOrder = interfaceInfoQueryRequest.getSortOrder();
            String description = interfaceInfoQuery.getDescription();

            interfaceInfoQuery.setDescription(null);
            if (size > 50) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
            queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
            queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                    sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
            Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size), queryWrapper);
            return ResultUtils.success(interfaceInfoPage);
        }

    /**
     * 发布
     * @param idRequest
     * @return
     */
    @PostMapping("/online")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> onLine(@RequestBody IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //校验接口是否存在
        long id = idRequest.getId();
        InterfaceInfo oldInfo = interfaceInfoService.getById(id);
        if (oldInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        com.xunhao.xhapiclientsdk.entity.User user = new com.xunhao.xhapiclientsdk.entity.User();
        String name = xhapiClient.getUserNameByPost(user);
        if (StringUtils.isBlank(name)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"接口调用失败");
        }
        //修改上下线状态
        InterfaceInfo info = new InterfaceInfo();
        info.setId(id);
        info.setStatus(InterfaceInfoEnum.ONLINE.getValue());
        boolean result = interfaceInfoService.updateById(info);
        return ResultUtils.success(result);
    }

    @PostMapping("/unline")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> unLine(@RequestBody IdRequest idRequest) {
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //校验接口是否存在
        long id = idRequest.getId();
        InterfaceInfo oldInfo = interfaceInfoService.getById(id);
        if (oldInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        //修改上下线状态
        InterfaceInfo info = new InterfaceInfo();
        info.setId(id);
        info.setStatus(InterfaceInfoEnum.UNLINE.getValue());
        boolean result = interfaceInfoService.updateById(info);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo info = interfaceInfoService.getById(id);
        if (info == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(info);
    }

    /**
     * 调用接口
     * @param invokeRequest
     * @param request
     * @return
     */
    @PostMapping("/invoke")
    public BaseResponse<Object> invokeInterfaceInfo(@RequestBody InterfaceInfoInvokeRequest invokeRequest, HttpServletRequest request) {
        //检查请求对象是否为空或请求id是否小于等于0
        if (invokeRequest == null || invokeRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //获取接口id
        long id = invokeRequest.getId();
        //获取用户请求参数
        String userRequestParams = invokeRequest.getUserRequestParams();
        //判断是否存在
        InterfaceInfo oldInfo = interfaceInfoService.getById(id);
        if (oldInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        //检查接口状态是否为下线状态
        if (oldInfo.getStatus().equals(InterfaceInfoEnum.UNLINE.getValue())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口已关闭");
        }
        //获取当前登录用户的ak和sk，这样相当于用户自己的这个身份去调用
        //也不会担心它刷接口，因为知道是谁刷了这个接口，安全
        User loginUser = userService.getLoginUser(request);
        String accessKey = loginUser.getAccessKey();
        String secretKey = loginUser.getSecretKey();
        XhapiClient client = new XhapiClient(accessKey, secretKey);
        //我们只需要进行测试调用，所以我们需要解析传递过来的参数
        Gson gson = new Gson();
        //将用户请求参数转换为User对象
        com.xunhao.xhapiclientsdk.entity.User user = gson.fromJson(userRequestParams, com.xunhao.xhapiclientsdk.entity.User.class);
        //调用client的getUsernameByPost方法,传入用户对象，获取用户名
        // TODO 需要修改为根据测试地址调用
        String usernameByPost = client.getUserNameByPost(user);
        return ResultUtils.success(usernameByPost);
    }

    }