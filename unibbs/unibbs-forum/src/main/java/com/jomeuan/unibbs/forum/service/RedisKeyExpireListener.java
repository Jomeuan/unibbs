package com.jomeuan.unibbs.forum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.jomeuan.unibbs.domain.PostDo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RedisKeyExpireListener extends KeyExpirationEventMessageListener {

    // 通过构造函数注入 RedisMessageListenerContainer 给 KeyExpirationEventMessageListener
    public RedisKeyExpireListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;
    @Autowired
    private PostService postService;

    @Override
    public void doHandleMessage(Message message) {
        String keyName = new String(message.getBody());
        if (keyName.startsWith("COMMENT_")) {
            String[] split = keyName.split(":");;
            Assert.isTrue(split.length==2, "reddis expire key name error");

            Long actionId = Long.parseLong(split[1]);
            PostDo rootPostDo = postService.getRootPost(actionId);
            BoundZSetOperations<Object,Object> zset = redisTemplate.boundZSetOps("rankings");
            zset.incrementScore(rootPostDo.getAction().getId(), -1);
            if (zset.score(rootPostDo.getAction().getId())==0) {
                zset.remove(rootPostDo.getAction().getId());
            }
        }

    }
}