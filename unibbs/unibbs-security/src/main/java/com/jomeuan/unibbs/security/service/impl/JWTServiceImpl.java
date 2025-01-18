package com.jomeuan.unibbs.security.service.impl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jomeuan.unibbs.security.domain.UserAuthentication;
import com.jomeuan.unibbs.security.entity.Role;
import com.jomeuan.unibbs.security.entity.User;
import com.jomeuan.unibbs.security.entity.UserRole;
import com.jomeuan.unibbs.security.mapper.RoleMapper;
import com.jomeuan.unibbs.security.mapper.UserMapper;
import com.jomeuan.unibbs.security.mapper.UserRoleMapper;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;

@Service
public class JWTServiceImpl {

    private static final SecretKey key = Jwts.SIG.HS256.key().build();

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    public UserAuthentication getUserAuthentication(String userAccount) {
        UserAuthentication res = new UserAuthentication();
        User user = userMapper.selectOne(Wrappers.lambdaQuery(User.class).eq(User::getAccount, userAccount));
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        List<Role> roles = userRoleMapper
                .selectList(Wrappers.lambdaQuery(UserRole.class).eq(UserRole::getUserId, user.getId()))
                .stream().map(userRole -> roleMapper.selectById(userRole.getRoleId()))
                .collect(Collectors.toList());
        res.setUserId(user.getId());
        res.setAccount(userAccount);
        res.setName(user.getName());
        res.setRoles(roles);
        return res;
    }

    public String buildJWT(Object object) {
        JwtBuilder jwtBuilder = Jwts.builder();
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                jwtBuilder.claim(field.getName(), field.get(object));
            } catch (IllegalArgumentException |IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return jwtBuilder.signWith(key).compact();
    }
}
