package com.jomeuan.unibbs.forum.mapper;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jomeuan.unibbs.forum.domain.PostDo;
import com.jomeuan.unibbs.forum.domain.PostMapperOrder;
import com.jomeuan.unibbs.forum.entity.ActionPo;
import com.jomeuan.unibbs.forum.vo.PostVo;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class MapperTest {
    @Autowired
    ActionMapper actionMapper;

    @Autowired
    PostMapper postMapper;
    @Test 
    void actionMapperTest(){
        List<PostVo> posts = actionMapper.selectCommentPostByTargetAction(
            1869647202599395330L, 
                    PostMapperOrder.PUBLISH_TIME,
                    true, 
                    0, 
                    20);
        // log.info(String.valueOf(posts.size()));
        // posts.forEach(post->log.info(post.getContent()));
    }


}
