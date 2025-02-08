package com.jomeuan.unibbs.profile.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jomeuan.unibbs.profile.domain.UserAuthentication;
import com.jomeuan.unibbs.profile.service.JWTService;
import com.jomeuan.unibbs.profile.vo.R;

import io.jsonwebtoken.JwtException;
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

        httpServletResponse.setContentType("application/json;charset=utf-8");

        String token = httpServletRequest.getHeader("token");

        ObjectMapper objectMapper = new ObjectMapper();

        System.out.println("profile filter started");

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

            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            userAuthentication.getRoles()
                    .forEach(rolePo -> authorities.add(new SimpleGrantedAuthority("ROLE_" + rolePo.getName())));

            // 注意password为空
            var authentication = new UsernamePasswordAuthenticationToken(userAuthentication.getUser().getAccount(),
                    userAuthentication.getUser().getPassword(), authorities);

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