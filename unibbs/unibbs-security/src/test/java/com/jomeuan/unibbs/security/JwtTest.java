package com.jomeuan.unibbs.security;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jomeuan.unibbs.domain.Roles;
import com.jomeuan.unibbs.domain.UserAuthentication;
import com.jomeuan.unibbs.entity.UserPo;
import com.jomeuan.unibbs.util.JWTService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class JwtTest {

    @Autowired
    JWTService  jwtService;

    @Test
    void buildJWTTest() throws Exception {
        UserAuthentication res = new UserAuthentication();
        res.setUser(new UserPo());
        res.getUser().setId(1L);
        res.getUser().setAccount("111");
        res.setRoles(List.of(Roles.VISITOR_ROLE));

        String jwt = jwtService.buildJWT("userAuthentication",res);
        log.info("****************");
        log.info(jwt);
        
        // 验证 JWT
        res=jwtService.parseJWT(jwt);
        log.info(res.getUser().getAccount());

    }
}
