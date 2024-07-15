package com.xunhao.project.model.dto.interfaceInfo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class InterfaceInfoInvokeRequest implements Serializable {

    private Long id;

    private String userRequestParams;
}