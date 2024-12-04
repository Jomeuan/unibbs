package com.jomeuan.unibbs.service.impl;

import com.jomeuan.unibbs.entity.User;
import com.jomeuan.unibbs.mapper.UserMapper;
import com.jomeuan.unibbs.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jomeuan
 * @since 2024-12-03
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
