package com.jomeuan.unibbs.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableFeignClients
@EnableDiscoveryClient
public class UnibbsSecurityApplication {
    public static void main(String[] args) {
        SpringApplication.run(UnibbsSecurityApplication.class, args);
    }
    
}
