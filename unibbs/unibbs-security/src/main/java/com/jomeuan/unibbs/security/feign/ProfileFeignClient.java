package com.jomeuan.unibbs.security.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.jomeuan.unibbs.security.entity.ProfilePo;
import com.jomeuan.unibbs.security.vo.UserVo;

@FeignClient(name = "unibbs-profile", url = "http://localhost:9999")
public interface ProfileFeignClient {
    @GetMapping("/profile")
    public Object getProfile(@RequestParam Long userId);

    @PostMapping("/profile")
    public Object newProfile(@RequestBody ProfilePo profilePo,@RequestHeader("token") String token);
}
