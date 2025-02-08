package com.jomeuan.unibbs.security.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jomeuan.unibbs.security.domain.Roles;
import com.jomeuan.unibbs.security.domain.UserAuthentication;
import com.jomeuan.unibbs.security.entity.RolePo;
import com.jomeuan.unibbs.security.entity.UserPo;
import com.jomeuan.unibbs.security.entity.UserRolePo;
import com.jomeuan.unibbs.security.feign.ProfileFeignClient;
import com.jomeuan.unibbs.security.mapper.RoleMapper;
import com.jomeuan.unibbs.security.mapper.UserMapper;
import com.jomeuan.unibbs.security.mapper.UserRoleMapper;
import com.jomeuan.unibbs.security.util.IdGenerator;
import com.jomeuan.unibbs.security.vo.UserVo;

import io.seata.spring.annotation.GlobalTransactional;

@Service
public class UserAuthenticationService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    

    

    public UserAuthentication getUserAuthentication(String userAccount) {
        UserAuthentication res = new UserAuthentication();
        UserPo user = userMapper.selectOne(Wrappers.lambdaQuery(UserPo.class).eq(UserPo::getAccount, userAccount));
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        List<RolePo> roles = userRoleMapper
                .selectList(Wrappers.lambdaQuery(UserRolePo.class).eq(UserRolePo::getUserId, user.getId()))
                .stream().map(userRole -> roleMapper.selectById(userRole.getRoleId()))
                .collect(Collectors.toList());

        res.setUser(user);
        res.setRoles(roles);

        return res;
    }

    /**
     * 存储userPo,userRolePo
     * 
     * @param userAuthentication
     */
    @Transactional
    public void saveUserAndRole(UserAuthentication userAuthentication) {
        userMapper.insert(userAuthentication.getUser());
        userAuthentication.getRoles().forEach(role -> {
            userRoleMapper.insert(new UserRolePo(userAuthentication.getUser().getId(), role.getId()));
        });
    }

    
}
