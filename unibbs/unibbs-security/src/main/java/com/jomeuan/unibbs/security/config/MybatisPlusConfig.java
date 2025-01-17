package com.jomeuan.unibbs.security.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.jomeuan.unibbs.security.mapper")
public class MybatisPlusConfig {
    
}
