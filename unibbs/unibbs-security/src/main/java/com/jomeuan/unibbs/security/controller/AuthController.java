package com.jomeuan.unibbs.security.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jomeuan.unibbs.domain.Roles;
import com.jomeuan.unibbs.domain.UserAuthentication;
import com.jomeuan.unibbs.entity.ProfilePo;
import com.jomeuan.unibbs.entity.RolePo;
import com.jomeuan.unibbs.entity.UserPo;
import com.jomeuan.unibbs.entity.UserRolePo;
import com.jomeuan.unibbs.exception.AppException;
import com.jomeuan.unibbs.security.mapper.RoleMapper;
import com.jomeuan.unibbs.security.mapper.UserMapper;
import com.jomeuan.unibbs.security.mapper.UserRoleMapper;
import com.jomeuan.unibbs.security.service.UserAndProfileService;
import com.jomeuan.unibbs.util.IdGenerator;
import com.jomeuan.unibbs.util.JWTService;
import com.jomeuan.unibbs.vo.R;
import com.jomeuan.unibbs.vo.UserVo;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/security/auth")
public class AuthController {

    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserAndProfileService userAndProfileService;
    @Autowired
    private JWTService jwtService;


    /**
     * userVo中的密码没加密,在此处加密
     * 
     * @param userVo
     * @return
     * @throws IOException
     */
    @PostMapping("register")
    public Object register(@RequestBody UserVo userVo, HttpServletResponse response) throws IOException {

        final String lockKey = "register-lock";
        RLock rLock = redissonClient.getLock(lockKey);

        boolean locked;
        try {
            // 获取锁
            locked = rLock.tryLock(1, 10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(lockKey + "锁被中断了");
        }

        if (!locked) {
            throw new AppException("服务器繁忙:" + lockKey + "获取锁失败");
        }

        try {
            // - 先检查该userVo中的 account 是否已经存在
            if (userMapper.selectCount(
                    Wrappers.lambdaQuery(UserPo.class).eq(UserPo::getAccount,
                            userVo.getUser().getAccount())) != 0) {
                throw new AppException("账号已存在");
            }
            // - 预处理user对象
            UserPo userPo = userVo.getUser();
            userPo.setId(idGenerator.nextId());
            userPo.setPassword(passwordEncoder.encode(userPo.getPassword()));

            if (userVo.getRoles() == null) {
                userVo.setRoles(new ArrayList<>());
            }

            // -- 添加游客角色后,存储userAuthentication
            // -- contains 调用equals方法,故而重写了RolePo的equal方法
            if (!userVo.getRoles().contains(Roles.VISITOR_ROLE)) {
                userVo.getRoles().add(Roles.VISITOR_ROLE);
            }
            if (userVo.getProfile() == null)
                userVo.setProfile(new ProfilePo());

            userVo.getProfile().setId(userPo.getId());

            // -
            userAndProfileService.saveUserVo(userVo);
        } finally {
            rLock.unlock();
        }

        // TODO: 应该重定向到网关对应的登录页面
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
    public ResponseEntity<UserAuthentication> parseJWT(@RequestHeader("token") String jwt) {
        UserAuthentication res;
        try {
            res = jwtService.parseJWT(jwt);
        } catch (JwtException e) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok().body(res);
    }

}
