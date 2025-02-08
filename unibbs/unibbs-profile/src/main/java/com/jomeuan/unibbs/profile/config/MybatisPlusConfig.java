package com.jomeuan.unibbs.profile.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.jomeuan.unibbs.profile.mapper")
public class MybatisPlusConfig {
    
}
