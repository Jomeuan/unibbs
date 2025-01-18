package com.jomeuan.unibbs.security.domain;

import java.util.List;

import com.jomeuan.unibbs.security.entity.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserAuthentication{

    private Long userId;

    private String name;
    
    // 用来登录的账号
    private String account;

    private Integer state;

    private List<Role> roles;


}
