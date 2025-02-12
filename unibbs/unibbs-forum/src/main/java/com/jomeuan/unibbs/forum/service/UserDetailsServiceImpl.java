package com.jomeuan.unibbs.forum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jomeuan.unibbs.entity.RolePo;
import com.jomeuan.unibbs.entity.UserPo;
import com.jomeuan.unibbs.entity.UserRolePo;
import com.jomeuan.unibbs.forum.mapper.RoleMapper;
import com.jomeuan.unibbs.forum.mapper.UserMapper;
import com.jomeuan.unibbs.forum.mapper.UserRoleMapper;

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
        UserPo user = userMapper.selectOne(new QueryWrapper<UserPo>().eq("account", username));
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        Long roleId=userRoleMapper.selectOne(new QueryWrapper<UserRolePo>().eq("user_id", user.getId())).getRoleId();
        RolePo role = roleMapper.selectById(roleId);
        authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getName()));

        return new org.springframework.security.core.userdetails.User(username,user.getPassword(),authorities);
    }
    
}
