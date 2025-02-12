package com.jomeuan.unibbs.util;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;

@Component
public class IdGenerator {
    
    private static final IdentifierGenerator identifierGenerator = DefaultIdentifierGenerator.getInstance();

    /**
     * 雪花算法 生成的第一位是0,表示正数,所以Long类型(64位)刚刚好
     * @return
     */
    public Long nextId() {
        return identifierGenerator.nextId(null).longValue();
    }
}
