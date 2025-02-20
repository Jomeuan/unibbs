package com.jomeuan.unibbs.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jomeuan.unibbs.entity.ProfilePo;
import com.jomeuan.unibbs.entity.UserPo;
import com.jomeuan.unibbs.security.controller.AuthController;
import com.jomeuan.unibbs.security.mapper.UserMapper;
import com.jomeuan.unibbs.util.IdGenerator;
import com.jomeuan.unibbs.vo.UserVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class UnibbsSecurityApplicationTest {

    @Autowired
    AuthController registrationController;

    @Autowired
    UserMapper userMapper;

    @Test
    void signupTest() throws Exception {
        UserPo userPo = new UserPo();
        userPo.setAccount(String.valueOf(userMapper.selectCount(Wrappers.lambdaQuery(UserPo.class))));
        userPo.setPassword("123456");
        userMapper.insert(userPo);

        log.info(String.valueOf(userPo.getId()));
    }

    @Test
    @Transactional
    void signupRandomTest() throws Exception {
        Random random = new Random();
        UserPo userPo = new UserPo();
        userPo.setAccount(String.valueOf(random.nextInt(Integer.MAX_VALUE)));
        userPo.setPassword("123456");
        userMapper.insert(userPo);
        // throw new RuntimeException();
        log.info("********************************");
        log.info("\u001B[31m" + String.valueOf(userPo.getId()));
    }

    @Test
    void registerTest() throws Exception {
        UserVo userVo = new UserVo();

        Random random = new Random();
        UserPo userPo = new UserPo();
        userPo.setAccount(String.valueOf(random.nextInt(Integer.MAX_VALUE)));
        userPo.setPassword("123456");

        userVo.setUser(userPo);
        userVo.setRoles(null);
        userVo.setProfile(new ProfilePo());
        log.info("********************************");
        log.info(userPo.getAccount());
        registrationController.register(userVo, null);
    }

    @Test
    void zhongwen() throws Exception {
        log.info("********************************");
        log.info("中文");
    }

    @Autowired
    IdGenerator idGenerator;

    @Test
    void idGeneratorTest() throws Exception {
        List<Long> list = Collections.synchronizedList(new ArrayList<Long>());
        final int count=100; 
        CountDownLatch  latch = new CountDownLatch(count);
        for (int i = 0; i < count; i++) {
            new Thread(() -> {
                list.add(idGenerator.nextId());
                latch.countDown();
            });
        }
        latch.await();
        log.info("******************");

        System.out.println(list.stream().distinct().count());
        System.out.println(list.size());
    }

}
