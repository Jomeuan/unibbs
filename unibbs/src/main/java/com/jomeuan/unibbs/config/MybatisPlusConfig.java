package com.jomeuan.unibbs.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.jomeuan.unibbs.mapper")
public class MybatisPlusConfig {
    
}
