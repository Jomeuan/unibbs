package com.jomeuan.unibbs.security.service.impl;

import java.io.Reader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jomeuan.unibbs.security.domain.UserAuthentication;
import com.jomeuan.unibbs.security.entity.RolePo;
import com.jomeuan.unibbs.security.entity.UserPo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.AbstractDeserializer;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.lang.Maps;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {

    private static final SecretKey key = Keys
            .hmacShaKeyFor("unibbsunibbsunibbsunibbsunibbsunibbs".getBytes(StandardCharsets.UTF_8));

    public String buildJWT(String keyName, Object object) {
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.claim(keyName, object);
        return jwtBuilder.signWith(key).compact();
    }

    /**
     * 解析jwtString到UserAuthentication
     * 
     * @param jwtString
     * @return
     * @throws JwtException
     */
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
