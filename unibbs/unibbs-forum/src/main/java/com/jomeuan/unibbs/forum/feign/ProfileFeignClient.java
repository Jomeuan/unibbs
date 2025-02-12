package com.jomeuan.unibbs.forum.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jomeuan.unibbs.entity.ProfilePo;

@FeignClient(name = "unibbs-profile", url = "localhost:9999")
public interface ProfileFeignClient {
    @GetMapping("/profile")
    public ProfilePo getProfile(@RequestParam Long userId);
}
