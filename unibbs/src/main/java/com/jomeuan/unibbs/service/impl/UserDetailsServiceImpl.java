package com.jomeuan.unibbs.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jomeuan.unibbs.entity.Role;
import com.jomeuan.unibbs.entity.User;
import com.jomeuan.unibbs.entity.UserRole;
import com.jomeuan.unibbs.mapper.RoleMapper;
import com.jomeuan.unibbs.mapper.UserMapper;
import com.jomeuan.unibbs.mapper.UserRoleMapper;

import java.util.List;
import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("account", username));
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        Long roleId=userRoleMapper.selectOne(new QueryWrapper<UserRole>().eq("user_id", user.getId())).getRoleId();
        Role role = roleMapper.selectById(roleId);
        authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getName()));

        return new org.springframework.security.core.userdetails.User(username,user.getPassword(),authorities);
    }
    
}
