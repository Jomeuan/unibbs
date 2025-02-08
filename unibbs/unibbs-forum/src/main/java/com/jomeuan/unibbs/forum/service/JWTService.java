package com.jomeuan.unibbs.forum.service;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.jomeuan.unibbs.forum.domain.UserAuthentication;

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

}