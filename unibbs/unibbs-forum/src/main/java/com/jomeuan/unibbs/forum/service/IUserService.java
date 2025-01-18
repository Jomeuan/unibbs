package com.jomeuan.unibbs.forum.service;

import com.jomeuan.unibbs.forum.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jomeuan
 * @since 2024-12-03
 */
public interface IUserService extends IService<User> {
    void register(User user);
}
