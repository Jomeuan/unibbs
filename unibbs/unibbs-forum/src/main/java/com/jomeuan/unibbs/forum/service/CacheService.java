package com.jomeuan.unibbs.forum.service;

import java.util.Random;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.jomeuan.unibbs.entity.UserPo;

@Service
public class CacheService {

    Random random = new Random();
    
    @Cacheable(cacheNames = "test")
    public UserPo getFromCache(String key) {
        UserPo userPo = new UserPo();
        userPo.setId(random.nextLong(1, 1000));
        userPo.setAccount("User我");
        return userPo;
    }




}
