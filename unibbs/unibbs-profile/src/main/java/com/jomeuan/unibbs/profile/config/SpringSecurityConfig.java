package com.jomeuan.unibbs.profile.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.jomeuan.unibbs.util.jwt.JwtAuthenticationTokenFilter;

@EnableMethodSecurity(securedEnabled = true)
@Configuration
public class SpringSecurityConfig {

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(
                (authorizeHttpRequests) -> authorizeHttpRequests
                        // .requestMatchers("/profile/**").permitAll()
                        .anyRequest().authenticated());
        http.csrf((csrf) -> csrf.disable());
        http.cors(cors -> cors.configurationSource(corsConfiguration()));

        // 过滤器控制
        http.addFilterBefore(jwtAuthenticationTokenFilter, AuthorizationFilter.class);

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
