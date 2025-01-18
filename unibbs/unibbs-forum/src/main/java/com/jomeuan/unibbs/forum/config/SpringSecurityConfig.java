package com.jomeuan.unibbs.forum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

@EnableMethodSecurity
@Configuration
public class SpringSecurityConfig {

    /*
     * Caused by: java.lang.RuntimeException:
     * Could not postProcess
     * org.springframework.security.config.annotation.web.builders.WebSecurity@
     * 510689af
     * of type class
     * org.springframework.security.config.annotation.web.builders.WebSecurity
     * 
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(
                (authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers("/login/**").permitAll()
                        .requestMatchers("post/**").permitAll()
                        .anyRequest().authenticated());
        http.formLogin(Customizer.withDefaults());
        http.csrf((csrf) -> csrf.disable());

        return http.build();
    }

}
