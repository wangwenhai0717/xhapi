package com.xunhao.xhapicommon.service;

import com.xunhao.xhapicommon.model.entity.User;

public interface InnerUserService {

    /**
     * 接口查询ak、sk是否分配
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);
}
