package com.jomeuan.unibbs.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jomeuan.unibbs.entity.Action;
import com.jomeuan.unibbs.entity.UserRole;
import com.jomeuan.unibbs.mapper.ActionMapper;
import com.jomeuan.unibbs.mapper.UserRoleMapper;
import com.jomeuan.unibbs.service.IActionService;
import com.jomeuan.unibbs.service.IUserRoleService;
import com.jomeuan.unibbs.service.IUserService;


@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService{
    
}
