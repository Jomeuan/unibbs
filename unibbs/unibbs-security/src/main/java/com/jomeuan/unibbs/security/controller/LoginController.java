package com.jomeuan.unibbs.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jomeuan.unibbs.domain.UserAuthentication;
import com.jomeuan.unibbs.entity.UserPo;
import com.jomeuan.unibbs.security.service.UserAuthenticationService;
import com.jomeuan.unibbs.util.JWTService;
import com.jomeuan.unibbs.vo.JWTVo;
import com.jomeuan.unibbs.vo.R;
import com.jomeuan.unibbs.vo.UserVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class LoginController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;
    @Autowired
    UserAuthenticationService userAndRoleService;

    /**
     * 因为在springSecurityConfig中设定了permitall,所以是无过滤的进入到此函数中,
     * 所以要在经过authenticate(),
     * 因为AuthenticationManager是DaoAuthenticationProvider
     * authenticate()函数最后通过UserDetailsServiceImpl.loadUserByUsername()获取到userDetail
     */
    @PostMapping("/login/jwt")
    public Object login(@RequestBody UserVo userVo) {
        UserPo userPo = userVo.getUser();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userPo.getAccount(),
                userPo.getPassword());

        Authentication authenticate = authenticationManager.authenticate(token);

        if (authenticate != null && authenticate.isAuthenticated()) {
            // 登录成功
            UserAuthentication userAuthentication = userAndRoleService.getUserAuthentication(userPo.getAccount());
            // 不显示密码
            userAuthentication.getUser().setPassword(null);
            JWTVo res = new JWTVo(userAuthentication.getUser(),
                    jwtService.buildJWT("userAuthentication", userAuthentication));
            return R.ok(res);
        } else {
            //TODO:test

            return R.error("账号与密码出错");
        }
    }

}
