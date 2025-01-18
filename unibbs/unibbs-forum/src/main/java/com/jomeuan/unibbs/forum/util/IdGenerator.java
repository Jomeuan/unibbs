package com.jomeuan.unibbs.forum.util;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;

@Component
public class IdGenerator {
    private static final IdentifierGenerator identifierGenerator = DefaultIdentifierGenerator.getInstance();

    public Long nextId() {
        return identifierGenerator.nextId(null).longValue();
    }
}
