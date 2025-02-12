package com.jomeuan.unibbs.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@TableName("user_role")
public class UserRolePo {
    private Long userId;
    private Long roleId;
}
