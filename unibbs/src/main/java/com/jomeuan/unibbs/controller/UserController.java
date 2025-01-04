package com.jomeuan.unibbs.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jomeuan.unibbs.entity.User;
import com.jomeuan.unibbs.entity.UserRole;
import com.jomeuan.unibbs.entity.domain.Roles;
import com.jomeuan.unibbs.mapper.RoleMapper;
import com.jomeuan.unibbs.mapper.UserMapper;
import com.jomeuan.unibbs.mapper.UserRoleMapper;
import com.jomeuan.unibbs.service.IUserRoleService;
import com.jomeuan.unibbs.service.IUserService;
import com.jomeuan.unibbs.util.IdGenerator;
import com.jomeuan.unibbs.vo.BasicUserVo;
import com.jomeuan.unibbs.vo.R;

import jakarta.servlet.http.HttpServletResponse;

import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


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
     * @param httpResponse
     * @return
     */
    @PostMapping("register")
    public R<?> register(@RequestBody BasicUserVo userVo, HttpServletResponse httpResponse) {
        // 1. 先检查该userVo中的 username是否已经存在
        if (userService.getOne(new QueryWrapper<User>().eq("account", userVo.getAccount())) != null) {
            return R.error("该用户已存在");
        }

        return R.ok(null);
    }

    @GetMapping("testUser")
    public R<String> testUser() {
        return R.ok("test");
    }
    
}
