package com.jomeuan.unibbs.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@TableName("user_role")
public class UserRolePo implements Serializable {
    
    private static final long serialVersionUID = -2610876345221019744L;

    private Long userId;
    private Long roleId;
}
