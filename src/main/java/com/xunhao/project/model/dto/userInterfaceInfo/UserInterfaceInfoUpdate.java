package com.xunhao.project.model.dto.userInterfaceInfo;

import lombok.Data;

@Data
public class UserInterfaceInfoUpdate {

    /**
     * 调用用户 id
     */
    private Long userId;

    /**
     * 接口 id
     */
    private Long interfaceInfoId;
}
