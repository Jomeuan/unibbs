package com.jomeuan.unibbs.security.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jomeuan.unibbs.security.entity.RolePo;
import com.jomeuan.unibbs.security.entity.UserPo;
import com.jomeuan.unibbs.security.entity.UserRolePo;
import com.jomeuan.unibbs.security.mapper.RoleMapper;
import com.jomeuan.unibbs.security.mapper.UserMapper;
import com.jomeuan.unibbs.security.mapper.UserRoleMapper;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMapper roleMapper;
    
    @Override
    public UserDetails loadUserByUsername(String userAccount) throws UsernameNotFoundException {
        UserPo user = userMapper.selectOne(new QueryWrapper<UserPo>().eq("account", userAccount));
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + userAccount);
        }
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        for( UserRolePo userRolePo:userRoleMapper.selectList(new LambdaQueryWrapper<UserRolePo>().eq(UserRolePo::getUserId, user.getId()))){
            RolePo role = roleMapper.selectById(userRolePo.getRoleId());
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getName()));
        }

        return new org.springframework.security.core.userdetails.User(userAccount,user.getPassword(),authorities);
    }
}