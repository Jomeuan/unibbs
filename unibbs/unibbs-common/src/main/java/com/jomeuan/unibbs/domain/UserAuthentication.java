package com.jomeuan.unibbs.domain;

import java.util.List;

import com.jomeuan.unibbs.entity.RolePo;
import com.jomeuan.unibbs.entity.UserPo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthentication {

    private UserPo user;

    private List<RolePo> roles;

}
