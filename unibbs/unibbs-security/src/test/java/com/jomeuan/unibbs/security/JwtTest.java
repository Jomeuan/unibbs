package com.jomeuan.unibbs.security;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jomeuan.unibbs.security.domain.UserAuthentication;
import com.jomeuan.unibbs.security.entity.Role;
import com.jomeuan.unibbs.security.service.impl.JWTServiceImpl;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class JwtTest {

    @Autowired
    JWTServiceImpl  jwtService;

    @Test
    void buildJWTTest() throws Exception {
        UserAuthentication res = new UserAuthentication();
        res.setUserId(1L);
        res.setAccount("111");
        res.setName("John");
        res.setRoles(List.of(new Role(1L,"admin")));

        log.info(jwtService.buildJWT(res));
    }
}
