package com.jomeuan.unibbs.util.jwt;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest httpServletRequest,
            @NonNull HttpServletResponse httpServletResponse,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String token = httpServletRequest.getHeader("token");

        // 无token 返回一个anymous角色
        if (!StringUtils.hasText(token)) {
            JWTAuthentication jwtAuthentication = jwtService.anonymousAuthentication();
            SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
        } else {
            JWTAuthentication jwtAuthentication = jwtService.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
        }
        
        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }
}