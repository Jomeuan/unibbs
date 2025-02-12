package com.jomeuan.unibbs.forum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.jomeuan.unibbs.domain.UserAuthentication;

@FeignClient(name = "unibbs-security", url = "http://localhost:9999")
public interface SecurityFeignClient {
    @PostMapping("/security/auth/role")
    public Object addRole(@RequestBody UserAuthentication userAuthentication);
}
