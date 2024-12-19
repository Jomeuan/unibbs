package com.jomeuan.unibbs.service.impl;

import com.jomeuan.unibbs.entity.User;
import com.jomeuan.unibbs.entity.UserRole;
import com.jomeuan.unibbs.entity.domain.Roles;
import com.jomeuan.unibbs.mapper.UserMapper;
import com.jomeuan.unibbs.mapper.UserRoleMapper;
import com.jomeuan.unibbs.service.IUserService;
import com.jomeuan.unibbs.util.IdGenerator;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jomeuan
 * @since 2024-12-03
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public void register(User user) {
        //2. 插入user
        Long userId = idGenerator.nextId();
        user.setId(userId);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        userMapper.insert(user);
        // 插入user_role 
        userRoleMapper.insert(new UserRole(userId, Roles.USER_ROLE.getId()));
    }

}
