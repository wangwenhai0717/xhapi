package com.xunhao.project.model.dto.interfaceInfo;

import lombok.Data;

import java.io.Serializable;

@Data
public class InterfaceInfoAddRequest implements Serializable {
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
     * 主机名
     */
    private String host;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String requestResponse;

    /**
     * 请求类型
     */
    private String method;

    /**
     * 请求参数
     */
    private String requestParams;

}