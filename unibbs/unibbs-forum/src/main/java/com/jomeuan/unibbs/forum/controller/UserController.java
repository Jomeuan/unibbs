package com.jomeuan.unibbs.forum.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jomeuan.unibbs.forum.entity.User;
import com.jomeuan.unibbs.forum.mapper.RoleMapper;
import com.jomeuan.unibbs.forum.service.IUserRoleService;
import com.jomeuan.unibbs.forum.service.IUserService;
import com.jomeuan.unibbs.forum.util.IdGenerator;
import com.jomeuan.unibbs.forum.vo.BasicUserVo;
import com.jomeuan.unibbs.forum.vo.R;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jomeuan
 * @since 2024-12-03
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IUserService userService;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private IUserRoleService userRoleService;

    /**
     * 注册一个游客身份的user
     * 
     * @param userVo
     * @return
     */
    @PostMapping("register")
    public R<?> register(@RequestBody BasicUserVo userVo) {
        // 1. 先检查该userVo中的 username是否已经存在
        if (userService.getOne(new QueryWrapper<User>().eq("account", userVo.getAccount())) != null) {
            return R.error("该用户已存在");
        }
        //2.注册一个 ROLE 为 User 的用户
        userService.register(userVo);

        return R.ok(null);
    }

    @GetMapping("testUser")
    public R<String> testUser() {
        return R.ok("test");
    }
    
}
