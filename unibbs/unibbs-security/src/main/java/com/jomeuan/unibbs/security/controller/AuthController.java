package com.jomeuan.unibbs.security.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jomeuan.unibbs.security.domain.Roles;
import com.jomeuan.unibbs.security.domain.UserAuthentication;
import com.jomeuan.unibbs.security.entity.ProfilePo;
import com.jomeuan.unibbs.security.entity.RolePo;
import com.jomeuan.unibbs.security.entity.UserPo;
import com.jomeuan.unibbs.security.entity.UserRolePo;
import com.jomeuan.unibbs.security.feign.ProfileFeignClient;
import com.jomeuan.unibbs.security.mapper.RoleMapper;
import com.jomeuan.unibbs.security.mapper.UserMapper;
import com.jomeuan.unibbs.security.mapper.UserRoleMapper;
import com.jomeuan.unibbs.security.service.impl.JWTService;
import com.jomeuan.unibbs.security.service.impl.UserAndProfileService;
import com.jomeuan.unibbs.security.service.impl.UserAuthenticationService;
import com.jomeuan.unibbs.security.util.IdGenerator;
import com.jomeuan.unibbs.security.vo.R;
import com.jomeuan.unibbs.security.vo.UserVo;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/security/auth")
public class AuthController {

    @Autowired
    IdGenerator idGenerator;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserAuthenticationService userAuthenticationService;
    @Autowired
    UserAndProfileService userAndProfileService;
    @Autowired
    JWTService jwtService;

    @Autowired
    UserMapper userMapper;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    UserRoleMapper userRoleMapper;

    @Autowired
    ProfileFeignClient profileFeignClient;

    /**
     * userVo中的密码没加密,在此处加密
     * 
     * @param userVo
     * @return
     * @throws IOException
     */
    @PostMapping("register")
    public Object register(@RequestBody UserVo userVo, HttpServletResponse response) throws IOException {
        // 先检查该userVo中的 account 是否已经存在
        if (userMapper.selectCount(
                Wrappers.lambdaQuery(UserPo.class).eq(UserPo::getAccount, userVo.getUser().getAccount())) != 0) {
            return R.error("账号已存在");
        }
        UserPo userPo = userVo.getUser();
        userPo.setId(idGenerator.nextId());
        userPo.setPassword(passwordEncoder.encode(userPo.getPassword()));

        if (userVo.getRoles() == null) {
            userVo.setRoles(new ArrayList<>());
        }

        // 添加游客角色后,存储userAuthentication
        // contains 调用equals方法,故而重写了RolePo的equal方法
        if (!userVo.getRoles().contains(Roles.VISITOR_ROLE)) {
            userVo.getRoles().add(Roles.VISITOR_ROLE);
        }
        if (userVo.getProfile() == null)
            userVo.setProfile(new ProfilePo());

        userVo.getProfile().setId(userPo.getId());

        try {
            userAndProfileService.saveUserVo(userVo);
        } catch (RuntimeException e) {
            e.printStackTrace();
            if (response != null) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            return R.error(null);
        }

        // 应该重定向到网关对应的登录页面
        // response.sendRedirect("http://localhost:9010/login");
        userVo.getUser().setPassword(null);
        return R.ok(userVo);
    }

    // 添加用户权限
    @Secured(Roles.VISITOR_ROLE_NAME)
    @PostMapping("/role")
    public Object addRole(@RequestBody UserAuthentication userAuthentication, @RequestHeader("token") String jwt) {
        try {
            Assert.notNull(userAuthentication.getUser().getId(), "userId cannot be null");
            Assert.notNull(userAuthentication.getRoles(), "roles cannot be null");
            Assert.isTrue(userAuthentication.getUser().getId().equals(jwtService.parseJWT(jwt).getUser().getId()),
                    "jwt not equal to user");
        } catch (IllegalArgumentException | NullPointerException e) {
            return R.error(e.getMessage());
        }
        for (RolePo rolePo : userAuthentication.getRoles()) {
            try {
                userRoleMapper.insert(new UserRolePo(userAuthentication.getUser().getId(), rolePo.getId()));
            }
            // 已经存在则会返回DuplicateKeyException
            catch (DuplicateKeyException e) {
                continue;
            }
        }
        return R.ok(userAuthentication);
    }

    @Secured(Roles.VISITOR_ROLE_NAME)
    @GetMapping("parseJWT")
    public Object parseJWT(@RequestHeader("token") String jwt) {
        UserAuthentication res;
        try {
            res = jwtService.parseJWT(jwt);
        } catch (JwtException e) {
            return R.error("Token解析出错");
        }
        return R.ok(res);
    }

}
