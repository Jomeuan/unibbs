package com.jomeuan.unibbs.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.jomeuan.unibbs.security.domain.UserAuthentication;
import com.jomeuan.unibbs.security.feign.ProfileFeignClient;
import com.jomeuan.unibbs.security.vo.UserVo;

import io.seata.spring.annotation.GlobalTransactional;

@Service
public class UserAndProfileService {

    @Autowired
    private UserAuthenticationService userAndRoleService;
    @Autowired
    private JWTService jwtservice;

    @Autowired
    private ProfileFeignClient profileFeignClient;
    
    @GlobalTransactional
    public void saveUserVo(@RequestBody UserVo userVo) throws RuntimeException {
        UserAuthentication userAuthentication = new UserAuthentication(userVo.getUser(), userVo.getRoles());
        userAndRoleService.saveUserAndRole(userAuthentication);
        //创建一个临时token
        String token = jwtservice.buildJWT("userAuthentication", userAuthentication);
        // 存储profilePo
        profileFeignClient.newProfile(userVo.getProfile(),token);
    }
}
