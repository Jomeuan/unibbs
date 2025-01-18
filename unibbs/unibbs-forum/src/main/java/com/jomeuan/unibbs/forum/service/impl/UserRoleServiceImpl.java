package com.jomeuan.unibbs.forum.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jomeuan.unibbs.forum.entity.Action;
import com.jomeuan.unibbs.forum.entity.UserRole;
import com.jomeuan.unibbs.forum.mapper.ActionMapper;
import com.jomeuan.unibbs.forum.mapper.UserRoleMapper;
import com.jomeuan.unibbs.forum.service.IActionService;
import com.jomeuan.unibbs.forum.service.IUserRoleService;
import com.jomeuan.unibbs.forum.service.IUserService;


@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService{
    
}
