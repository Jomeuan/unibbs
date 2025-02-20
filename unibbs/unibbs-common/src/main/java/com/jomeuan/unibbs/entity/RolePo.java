package com.jomeuan.unibbs.entity;

import java.io.Serializable;
import java.util.Objects;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("role")
public class RolePo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -3189897147134064145L;

    /**
     * id:
     * 1:admin
     * 2:user
     */
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