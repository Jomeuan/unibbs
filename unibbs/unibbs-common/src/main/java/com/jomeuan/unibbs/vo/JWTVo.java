package com.jomeuan.unibbs.vo;


import com.jomeuan.unibbs.entity.UserPo;

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
