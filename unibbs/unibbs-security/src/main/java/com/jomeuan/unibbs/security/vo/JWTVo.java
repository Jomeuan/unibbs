package com.jomeuan.unibbs.security.vo;

import com.jomeuan.unibbs.security.entity.UserPo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JWTVo {
    UserPo user;
    String token;
}
