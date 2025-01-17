package com.jomeuan.unibbs.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jomeuan.unibbs.security.domain.UserAuthentication;
import com.jomeuan.unibbs.security.service.impl.JWTServiceImpl;
import com.jomeuan.unibbs.security.vo.BasicUserVo;

@RestController
public class LoginController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTServiceImpl jwtService;

    /**
     * 因为在springSecurityConfig中设定了permitall,所以是无过滤的进入到此函数中,
     * 所以要在经过authenticate(),
     * 因为AuthenticationManager是DaoAuthenticationProvider
     * authenticate()函数最后通过UserDetailsServiceImpl.loadUserByUsername()获取到userDetail
     */
    @PostMapping("/login/jwt")
    public String login(@RequestBody BasicUserVo basicUserVo) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(basicUserVo.getAccount(),
                basicUserVo.getPassword());
        Authentication authenticate = authenticationManager.authenticate(token);

        // return "hello";
        if (authenticate != null && authenticate.isAuthenticated()) {
            // 登录成功
            UserAuthentication userAuthentication = jwtService.getUserAuthentication(basicUserVo.getAccount());
            return jwtService.buildJWT(userAuthentication);
        } else {
            System.out.println("账号与密码出错");
            throw new RuntimeException("账号与密码出错");
        }

    }
}
