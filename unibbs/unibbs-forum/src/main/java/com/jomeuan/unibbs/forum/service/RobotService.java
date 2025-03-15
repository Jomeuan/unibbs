package com.jomeuan.unibbs.forum.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.jomeuan.unibbs.domain.ActionType;
import com.jomeuan.unibbs.domain.LikeDo;
import com.jomeuan.unibbs.domain.PostDo;
import com.jomeuan.unibbs.domain.Roles;
import com.jomeuan.unibbs.entity.ActionPo;
import com.jomeuan.unibbs.entity.CommentPo;
import com.jomeuan.unibbs.entity.ProfilePo;
import com.jomeuan.unibbs.forum.controller.PostController;
import com.jomeuan.unibbs.util.jwt.JWTAuthentication;
import com.jomeuan.unibbs.vo.PostVo;
import com.jomeuan.unibbs.vo.R;

import jakarta.annotation.PostConstruct;

@Service
public class RobotService {

    @Autowired
    private OllamaChatModel ollamaChatModel;

    @Autowired
    private PostController postController;

    // "robot_user_id":1889299949896417281
    private final Long robot_user_id = 1889299949896417281L;
    private final String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyQXV0aGVudGljYXRpb24iOnsidXNlciI6eyJpZCI6MTg4OTI5OTk0OTg5NjQxNzI4MSwiYWNjb3VudCI6InJvYm90IiwicGFzc3dvcmQiOm51bGwsInN0YXRlIjpudWxsLCJleGlwcmF0aW9uIjpudWxsfSwicm9sZXMiOlt7ImlkIjoyLCJuYW1lIjoiVklTSVRPUiJ9XX19.TPAUAaJfzw_MGGYtPQCn8MAs-NdlkJx_Ytmy5ntPF68";
    private final ProfilePo robotProfile = new ProfilePo();

    @PostConstruct
    public void initRobotProfile() {
        robotProfile.setId(robot_user_id);
        robotProfile.setNickname("机器人");
    }

    /**
     * postDo 是origin post ,robot在其 评论树 评论20条
     * 
     * @param postDo
     * @return
     */
    @Async
    public void startRobotChat(PostVo postVo) {

        SecurityContextHolder.getContext().setAuthentication(
                new JWTAuthentication(jwt, List.of(new SimpleGrantedAuthority(Roles.VISITOR_ROLE_NAME))));

        List<PostVo> list = new ArrayList<>(21);
        list.add(postVo);

        Random random = new Random();

        for (int i = 0; i < 20; ++i) {
            PostVo targetPostVo = list.get(random.nextInt(0, list.size()));
            ActionPo newActionPo = new ActionPo();
            newActionPo.setType(ActionType.COMMENT);
            newActionPo.setTargetId(targetPostVo.getThisPost().getAction().getId());
            newActionPo.setUserId(robot_user_id);

            CommentPo newCommentPo = new CommentPo();
            newCommentPo.setContent(ollamaChatModel.call(targetPostVo.getThisPost().getComment().getContent()));


            list.add(postController.publishPost(new PostDo(newActionPo, newCommentPo,robotProfile), jwt).getBody());
        }

    }

}
