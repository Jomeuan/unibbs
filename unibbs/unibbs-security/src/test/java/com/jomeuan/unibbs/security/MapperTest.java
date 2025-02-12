package com.jomeuan.unibbs.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jomeuan.unibbs.domain.Roles;
import com.jomeuan.unibbs.security.mapper.RoleMapper;

@SpringBootTest
public class MapperTest {
    
    @Autowired
    private RoleMapper roleMapper;


    @Test
    void roleMapperTest() throws Exception {
        roleMapper.insert(Roles.VISITOR_ROLE);
    }

}
