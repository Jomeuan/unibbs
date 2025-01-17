package com.jomeuan.unibbs.security.domain;

import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.jomeuan.unibbs.security.entity.Role;
import com.jomeuan.unibbs.security.entity.User;

import lombok.AllArgsConstructor;
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
