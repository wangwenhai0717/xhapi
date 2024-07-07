package com.xunhao.project.model.vo;

import com.xunhao.xhapicommon.model.entity.InterfaceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InterfaceInfoVo extends InterfaceInfo {

    private static final long serialVersionUID = 1L;

    private Integer totalNum;
}
