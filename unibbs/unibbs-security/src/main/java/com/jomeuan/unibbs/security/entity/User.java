package com.jomeuan.unibbs.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.Data;

@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;
    
    // 用来登录的账号
    private String account;

    private String password;

    //可以为null,TODO: 用户的状态控制
    private Integer state;

}
