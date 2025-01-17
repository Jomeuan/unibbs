package com.jomeuan.unibbs.security.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Role {
    /**
     * id:
     *  1:admin
     *  2:user
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;
}
