package com.jomeuan.unibbs.forum;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import com.jomeuan.unibbs.entity.ThreadPo;
import com.jomeuan.unibbs.forum.controller.PostController;
import com.jomeuan.unibbs.forum.mapper.ThreadMapper;
import com.jomeuan.unibbs.forum.service.CacheService;
import com.jomeuan.unibbs.forum.service.PostService;
import com.jomeuan.unibbs.util.IdGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
// @EnableCaching
@SpringBootTest
class UnibbsForumApplicationTests {

    @Autowired
    IdGenerator idGenerator;


    @Autowired
    PostController postController;

    @Autowired
    PostService postService;


    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    public void redisTest() {
        
        // 设置
        this.stringRedisTemplate.opsForValue().set("title", "spring 中文网", Duration.ofMinutes(5));

        // 读取
        String val = this.stringRedisTemplate.opsForValue().get("title");
        log.info("****************************");
        log.info(val);

    }

    @Autowired private CacheService cacheService;
    @Test
    public void cacheServiceTest() {

        String key="k";

        for(int i = 0; i < 10; i++){
            log.info(cacheService.getFromCache(key).getAccount());
        }
    }

    @Autowired RedisTemplate<Object, Object> redisTemplate;

    @Test
    public void redisTemplateTest() {
        ZSetOperations<Object,Object> zset = redisTemplate.opsForZSet();
    }

    @Autowired private ThreadMapper threadMapper;
    @Test
    public void threadPoTest() throws Exception {
        ThreadPo threadPo = new ThreadPo();
        threadPo.setId(idGenerator.nextId());
        threadPo.setTitle("测试common");
        threadMapper.insert(threadPo);
    }

    @Autowired RedissonClient redissonClient;

    @Test
    public void redissonTest() {

        log.info("********************************");
        //获取所有的key
        redissonClient.getKeys().getKeys().forEach(key -> log.info(key));

    }


}
