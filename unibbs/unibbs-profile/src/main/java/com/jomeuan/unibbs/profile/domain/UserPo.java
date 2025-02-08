package com.jomeuan.unibbs.profile.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    // 用来登录的账号
    private String account;

    private String password;

    //可以为null,TODO: 用户的状态控制
    private Integer state;

    private LocalDateTime exipration;

}
