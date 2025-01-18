package com.jomeuan.unibbs.forum.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.jomeuan.unibbs.forum.mapper")
public class MybatisPlusConfig {
    
}
