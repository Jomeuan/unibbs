package com.jomeuan.unibbs.vo;

import java.util.List;

import org.springframework.context.annotation.Profile;

import com.jomeuan.unibbs.entity.ProfilePo;
import com.jomeuan.unibbs.entity.RolePo;
import com.jomeuan.unibbs.entity.UserPo;

import lombok.Data;

/**
 * 包含了登录认证的信息(账号,密码,角色)、profile信息
 */
@Data
public class UserVo{
    private UserPo user;

    private List<RolePo> roles;

    private ProfilePo profile;

}
