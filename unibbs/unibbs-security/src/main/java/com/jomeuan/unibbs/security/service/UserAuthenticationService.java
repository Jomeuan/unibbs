package com.jomeuan.unibbs.security.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jomeuan.unibbs.domain.UserAuthentication;
import com.jomeuan.unibbs.entity.RolePo;
import com.jomeuan.unibbs.entity.UserPo;
import com.jomeuan.unibbs.entity.UserRolePo;
import com.jomeuan.unibbs.security.mapper.RoleMapper;
import com.jomeuan.unibbs.security.mapper.UserMapper;
import com.jomeuan.unibbs.security.mapper.UserRoleMapper;

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
