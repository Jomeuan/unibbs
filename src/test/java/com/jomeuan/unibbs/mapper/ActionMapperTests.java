package com.jomeuan.unibbs.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jomeuan.unibbs.entity.Action;
import com.jomeuan.unibbs.entity.domain.PostDo;
import com.jomeuan.unibbs.service.IActionService;
@SpringBootTest
public class ActionMapperTests {

    @Autowired
    IActionService actionService;

    @Autowired
    ActionMapper actionMapper;

    @Test
    void testSelectPostByActionId() {
        List<PostDo> posts = actionMapper.selectPostByActionId(1866514116579778562L, "time", true);
        for (PostDo post : posts) {
            System.out.println(post.getContent());
        }
    }


}
