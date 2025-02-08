package com.jomeuan.unibbs.profile.domain;

import java.util.List;

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
