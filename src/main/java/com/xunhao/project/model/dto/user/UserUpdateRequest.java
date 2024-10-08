package com.xunhao.project.model.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserUpdateRequest implements Serializable {

    private Long id;

    private String userAccount;

    private static final long serialVersionUID = 1L;
}