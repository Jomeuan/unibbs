package com.jomeuan.unibbs;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jomeuan.unibbs.controller.PostController;
import com.jomeuan.unibbs.controller.UserController;
import com.jomeuan.unibbs.entity.Action;
import com.jomeuan.unibbs.entity.User;
import com.jomeuan.unibbs.entity.domain.PostDo;
import com.jomeuan.unibbs.service.IActionService;
import com.jomeuan.unibbs.service.IPostService;
import com.jomeuan.unibbs.service.IUserService;
import com.jomeuan.unibbs.util.IdGenerator;
import com.jomeuan.unibbs.vo.BasicUserVo;
import com.jomeuan.unibbs.vo.PostVo;

@SpringBootTest
class UnibbsApplicationTests {
    @Autowired
    IActionService actionService;

    @Autowired
    IdGenerator idGenerator;

    @Autowired
    IUserService userService;

    @Autowired 
    PostController postController;

    @Autowired
    IPostService postService;

    @Test
    void contextLoads() {
        System.out.println(actionService);
    }

    @Test
    void idTest() {
        System.out.println(idGenerator.nextId());
        System.out.println(Long.MAX_VALUE);
    }

    @Test
    void registerTest() {
        BasicUserVo userVo = new BasicUserVo();
        userVo.setName("user1");
        userVo.setAccount("111");
        userVo.setPassword("123456");
        userService.register(userVo);    
    }

    @Test
    void publishPostTest(){
        Long userId = userService.getOne(new QueryWrapper<User>().eq("name","user1")).getId();
        // 第一个是发文
        PostVo article =new PostVo();
        article.setUserId(userId);
        article.setType(Action.ActionType.COMMENT.name());
        article.setTargetId(1L);
        article.setTime(LocalDateTime.now());
        article.setContent("这是user1的发文 time: " + article.getTime().toString());
        
        article=new PostVo(postService.save(article.toPostBo()));
        //5个对发文的评论
        for(int i=0; i<5; i++){
            PostVo comment = new PostVo();
            comment.setUserId(userId);
            comment.setType(Action.ActionType.COMMENT.name());
            comment.setContent("这是user1的第" + (i+1) + "个评论");
            comment.setTime(LocalDateTime.now());
            comment.setTargetId(article.getActionId());
            postController.publishcomment(comment);
        }

        System.out.println(article.getActionId());
    }
}
