package com.jomeuan.unibbs.security.domain;

import java.util.List;

import com.jomeuan.unibbs.security.entity.RolePo;
import com.jomeuan.unibbs.security.entity.UserPo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthentication{

    private UserPo user;

    private List<RolePo> roles;

}
