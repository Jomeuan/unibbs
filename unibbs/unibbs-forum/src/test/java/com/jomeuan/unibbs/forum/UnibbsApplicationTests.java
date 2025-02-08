package com.jomeuan.unibbs.forum;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jomeuan.unibbs.forum.controller.PostController;
import com.jomeuan.unibbs.forum.service.PostService;
import com.jomeuan.unibbs.forum.util.IdGenerator;

@SpringBootTest
class UnibbsApplicationTests {

    @Autowired
    IdGenerator idGenerator;


    @Autowired
    PostController postController;

    @Autowired
    PostService postService;

    @Test
    void idTest() {
        System.out.println(idGenerator.nextId());
        System.out.println(Long.MAX_VALUE);
    }

    @Test
    void publishPostTest() {

    }

}
