package com.jomeuan.unibbs.security.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.jomeuan.unibbs.util.jwt.JwtAuthenticationTokenFilter;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

@EnableMethodSecurity(securedEnabled = true)
@Configuration
public class SpringSecurityConfig {
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Bean
    AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        ProviderManager pm = new ProviderManager(daoAuthenticationProvider);
        return pm;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf((csrf) -> csrf.disable());

        http.authorizeHttpRequests(
                (authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers("/login/**").permitAll()
                        .requestMatchers("/security/auth/register").permitAll()
                        .anyRequest().authenticated());

        // 用户登录控制
        // http.formLogin()
        // .failureHandler(new MyAuthenticationFailureHandler())
        // .successHandler(new MyAuthenticationSuccessHandler());

        // http.cors(Customizer.withDefaults());
        http.cors(cors->cors.configurationSource(corsConfiguration()));


        // 创建对象AuthenticationManager，交给容器管理，后面需要使用该对象完成自定义登录。
        http.sessionManagement(
                sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 过滤器控制
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Cors 的配置信息 配置+路径
     */
    CorsConfigurationSource corsConfiguration() {
        // Cors配置类
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 是否返回时生成凭证
        corsConfiguration.setAllowCredentials(false);
        // corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        // corsConfiguration.setAllowedMethods(Arrays.asList("*"));
        // corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
        // 设置注册URL 配置类
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }

    

}
