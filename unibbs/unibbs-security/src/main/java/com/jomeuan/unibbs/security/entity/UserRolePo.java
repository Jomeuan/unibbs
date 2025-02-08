package com.jomeuan.unibbs.security.entity;

import org.apache.ibatis.type.Alias;

import com.baomidou.mybatisplus.annotation.IdType;
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
