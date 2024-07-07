package com.xunhao.project.model.dto.interfaceInfo;

import lombok.Data;

import java.io.Serializable;

@Data
public class InterfaceInfoInvokeRequest implements Serializable {

    private Long id;

    /**
     * 用户请求参数
     */
    private String userRequestParams;

}