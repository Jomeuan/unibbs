package com.jomeuan.unibbs.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class UserPo implements Serializable {


    
    private static final long serialVersionUID = -4732087812190984052L;

    private Long id;

    // 用来登录的账号
    private String account;

    private String password;

    //可以为null,TODO: 用户的状态控制
    private Integer state;

    private LocalDateTime exipration;

}
