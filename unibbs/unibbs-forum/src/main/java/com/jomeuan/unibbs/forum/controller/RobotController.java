package com.jomeuan.unibbs.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.jomeuan.unibbs.domain.PostDo;
import com.jomeuan.unibbs.forum.service.RobotService;
import com.jomeuan.unibbs.vo.PostVo;

@RestController
public class RobotController {

    @Autowired
    PostController postController;

    @Autowired 
    RobotService robotService;

    @PostMapping("robot")
    public ResponseEntity<PostVo> startRobotChat(@RequestBody PostDo postDo, @RequestHeader("token") String jwt) {
        
        PostVo res=postController.publishPost(postDo, jwt).getBody();
        robotService.startRobotChat(res);

        return ResponseEntity.ok(res);
    }

}
