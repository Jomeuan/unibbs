package com.jomeuan.unibbs.util.jwt;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.jomeuan.unibbs.domain.UserAuthentication;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class JWTAuthentication extends AbstractAuthenticationToken {

    private static final long serialVersionUID = -6894831232948585333L;

    private String token;

    private UserAuthentication userAuthentication;


    public JWTAuthentication(String token,Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.token = token;
    }


    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }
    
}
