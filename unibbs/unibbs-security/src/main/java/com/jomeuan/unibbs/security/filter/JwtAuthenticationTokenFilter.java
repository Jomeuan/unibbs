package com.jomeuan.unibbs.security.filter;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jomeuan.unibbs.domain.UserAuthentication;
import com.jomeuan.unibbs.util.JWTService;
import com.jomeuan.unibbs.vo.R;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain) throws ServletException, IOException {

        httpServletResponse.setContentType("application/json;charset=utf-8");

        String url = httpServletRequest.getRequestURI();
        ObjectMapper objectMapper = new ObjectMapper();

        if (url.startsWith("/login/jwt")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        } else if (url.equals("/security/auth/register")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        String token = httpServletRequest.getHeader("token");
        if (!StringUtils.hasText(token)) {
            String message = objectMapper.writeValueAsString(R.error("token 不能为空"));
            httpServletResponse.getWriter().write(message);
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // if(!JWTUtils.isExpired(token)){
        // httpServletResponse.getWriter().write("token 失效");
        // return;
        // }

        try {
            UserAuthentication userAuthentication = jwtService.parseJWT(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(userAuthentication.getUser().getAccount());
            if (userDetails == null) {
                httpServletResponse.getWriter().write("token校验失败，请重新登录");
                return;
            }
            var authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                    userDetails.getPassword(), userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException e) {
            logger.error(e.getMessage(), e);
            String message = objectMapper.writeValueAsString(R.error("token 无效，请重新登录"));
            httpServletResponse.getWriter().write(message);
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}