package com.jomeuan.unibbs.profile.domain;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolePo {
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
