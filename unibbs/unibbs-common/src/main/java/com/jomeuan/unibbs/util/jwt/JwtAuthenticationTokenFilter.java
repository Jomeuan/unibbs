package com.jomeuan.unibbs.util.jwt;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
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
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain) throws ServletException, IOException {

        String token = httpServletRequest.getHeader("token");

        if (!StringUtils.hasText(token)) {
            try{
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            }catch(RuntimeException e){
                e.printStackTrace();
            }
        } else {
            JWTAuthentication jwtAuthentication = new JWTAuthentication(token, null);
            // 验证token,注意这里的jwtAuthentication被替换了
            jwtAuthentication = jwtService.authenticate(jwtAuthentication);
            // 没有异常则ok
            SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);

            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }

    }
}