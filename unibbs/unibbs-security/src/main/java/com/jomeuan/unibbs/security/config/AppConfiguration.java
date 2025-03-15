package com.jomeuan.unibbs.security.config;

import org.mybatis.spring.annotation.MapperScan;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@MapperScan("com.jomeuan.unibbs.security.mapper")
@ComponentScan({"com.jomeuan.unibbs.util","com.jomeuan.unibbs.exception"})
public class AppConfiguration {
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://127.0.0.1:6379")
                .setDatabase(0);
        return Redisson.create(config);
    }
}
