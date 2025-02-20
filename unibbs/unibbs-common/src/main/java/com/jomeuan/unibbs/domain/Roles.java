package com.jomeuan.unibbs.domain;

import com.jomeuan.unibbs.entity.RolePo;


public class Roles {
    
    public final static String ADMIN_ROLE_NAME ="ROLE_ADMIN";
    public final static String VISITOR_ROLE_NAME ="ROLE_VISITOR";
    public final static String MODERATOR_ROLE_NAME ="ROLE_MODERATOR";

    public final static RolePo ADMIN_ROLE = new RolePo(1L, "ADMIN");
    public final static RolePo VISITOR_ROLE = new RolePo(2L, "VISITOR");
    public final static RolePo MODERATOR_ROLE = new RolePo(3L, "MODERATOR");

}
