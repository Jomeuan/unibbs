package com.jomeuan.unibbs.util.jwt;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jomeuan.unibbs.domain.Roles;
import com.jomeuan.unibbs.domain.UserAuthentication;
import com.jomeuan.unibbs.entity.UserPo;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.lang.Maps;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {

    // private static final SecretKey key = Jwts.SIG.HS256.key().build();
    private static final SecretKey key = Keys
            .hmacShaKeyFor("unibbsunibbsunibbsunibbsunibbsunibbs".getBytes(StandardCharsets.UTF_8));

    public String buildJWT(String keyName, Object object) {
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.claim(keyName, object);
        // TODO : 过期时间
        return jwtBuilder.signWith(key).compact();
    }

    public UserAuthentication parseJWT(String jwtString) throws JwtException {
        UserAuthentication res = Jwts.parser()
                .json(new JacksonDeserializer(Maps.of("userAuthentication", UserAuthentication.class).build()))
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwtString)
                .getPayload()
                .get("userAuthentication", UserAuthentication.class);
        return res;
    }

    /**
     * 校验jwtAuthentication 并且填充GrantedAuthority和UserAuthentication
     * 
     * @param
     * @return 新的jwtAuthentication
     * @throws AuthenticationException
     */
    public JWTAuthentication authenticate(String token) throws AuthenticationException {
        if (!StringUtils.hasText(token)) {
            throw new UsernameNotFoundException("token空白");
        }
        UserAuthentication userAuthentication;
        try {
            userAuthentication = this.parseJWT(token);
        } catch (JwtException e) {
            e.printStackTrace();
            throw new AccessDeniedException("token valid wtih: " + token);
        }

        List<GrantedAuthority> grantedAuthorities = userAuthentication.getRoles().stream()
                .map(rolePo -> new SimpleGrantedAuthority("ROLE_" + rolePo.getName())).collect(Collectors.toList());

        JWTAuthentication res = new JWTAuthentication(token, grantedAuthorities);
        res.setAuthenticated(true);

        return res;
    }

    private JWTAuthentication anonymousJwtAuthentication;

    public JWTAuthentication anonymousAuthentication() {

        if (anonymousJwtAuthentication == null) {

            UserAuthentication userAuthentication = new UserAuthentication(new UserPo(null, "anymous", null, 1, null),
                    List.of(Roles.ANONYMOUS_ROLE));
            anonymousJwtAuthentication = new JWTAuthentication(
                    this.buildJWT("userAuthentication", userAuthentication),
                    null);
            anonymousJwtAuthentication.setUserAuthentication(userAuthentication);
            anonymousJwtAuthentication.setAuthenticated(true);
        }

        return anonymousJwtAuthentication;
    }

}