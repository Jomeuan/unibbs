package com.jomeuan.unibbs.security.entity;

import java.util.Objects;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("role")
public class RolePo {
    /**
     * id:
     * 1:admin
     * 2:user
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    @Override
    public boolean equals(Object rhs) {
        if (rhs == null)
            return false;
        else if (!(rhs instanceof RolePo)) {
            return false;
        }
        return this.id == ((RolePo) rhs).id;
    }
    @Override public int hashCode() {
        return Objects.hash(name, id);
    }

}
