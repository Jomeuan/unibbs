package com.jomeuan.unibbs.profile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableDiscoveryClient
@EnableTransactionManagement
@SpringBootApplication
public class UnibbsProfileApplication {
    public static void main(String[] args) {
        SpringApplication.run(UnibbsProfileApplication.class, args);
    }
}
