package com.xunhao.project.model.dto.interfaceInfo;

import lombok.Data;

import java.io.Serializable;

@Data
public class InterfaceInfoUpdateRequest implements Serializable {
    private Long id;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String requestResponse;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 接口状态（0-关闭，1-开启）
     */
    private Integer status;

    /**
     * 请求类型
     */
    private String method;

    /**
     * host
     */
    private String host;

}