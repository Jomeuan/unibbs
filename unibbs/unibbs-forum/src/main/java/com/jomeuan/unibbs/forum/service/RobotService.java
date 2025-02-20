package com.jomeuan.unibbs.forum.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
import com.jomeuan.unibbs.forum.controller.PostController;
import com.jomeuan.unibbs.vo.PostVo;
import com.jomeuan.unibbs.vo.R;

@Service
public class RobotService {

    @Autowired
    private PostController postController;

    // 行为缓存,长度大一点
    private ActionPo[] actions = new ActionPo[60];
    // 用来指向下一个action存放在actions的位置
    private AtomicInteger actionsIndex = new AtomicInteger(0);

    // "robot_user_id":1889299949896417281
    private final Long robot_user_id = 1889299949896417281L;
    private final String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyQXV0aGVudGljYXRpb24iOnsidXNlciI6eyJpZCI6MTg4OTI5OTk0OTg5NjQxNzI4MSwiYWNjb3VudCI6InJvYm90IiwicGFzc3dvcmQiOm51bGwsInN0YXRlIjpudWxsLCJleGlwcmF0aW9uIjpudWxsfSwicm9sZXMiOlt7ImlkIjoyLCJuYW1lIjoiVklTSVRPUiJ9XX19.TPAUAaJfzw_MGGYtPQCn8MAs-NdlkJx_Ytmy5ntPF68";
    // comment_broadcast to community_id=1887803077897834497
    private final Long communityId = 1887803077897834497L;

    @Scheduled(fixedDelay = 1000)
    public void robot() {

        ArrayList<GrantedAuthority> arr = new ArrayList<GrantedAuthority>();
        arr.add(new SimpleGrantedAuthority(Roles.VISITOR_ROLE_NAME));
        arr.add(new SimpleGrantedAuthority(Roles.MODERATOR_ROLE_NAME));

        var authentication = new UsernamePasswordAuthenticationToken("robot",
                "123456", arr);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 先抢到一个actions的位置
        int pos = (actionsIndex.getAndIncrement()) % actions.length;
        ActionPo action = new ActionPo();
        actions[pos] = action;

        Random random = new Random();

        // 填充action
        action.setUserId(robot_user_id);
        CommentPo comment = new CommentPo(null, "robot 的帖子" + LocalDateTime.now().toString(), 0, 0, 0, 0);

        // 随机获取行为
        int t = random.nextInt(0, 3);

        // comment_broadcast
        if (pos == 0 || t == 0) {
            action.setType(ActionType.COMMENT_BROADCAST);
            action.setTargetId(communityId);
            PostVo res = postController.publishPost(new PostDo(action, comment), jwt).getBody();;
            actions[pos] = res.getThisPost().getAction();
        }
        // comment
        else if (t == 1) {
            action.setType(ActionType.COMMENT);
            // 多线程情况下可能之前的action未被填充,而这个线程先执行到这
            Long targetId = actions[random.nextInt(0, pos)].getId();
            // 循环直到获取到不是null的targetActionId
            for (; targetId == null; targetId = actions[random.nextInt(0, pos)].getId())
                ;
            action.setTargetId(targetId);
            PostVo res = postController.publishPost(new PostDo(action, comment), jwt).getBody();;
            actions[pos] = res.getThisPost().getAction();
        }
        // like
        else if (t == 2) {
            action.setType(ActionType.LIKE);
            action.setContentId(1L);

            // 多线程情况下可能之前的action未被填充,而这个线程先执行到这
            Long targetId = actions[random.nextInt(0, pos)].getId();
            // 循环直到获取到不是null的targetActionId
            for (; targetId == null; targetId = actions[random.nextInt(0, pos)].getId())
                ;
            action.setTargetId(targetId);

            R<LikeDo> res = (R<LikeDo>) postController.publishLike(new LikeDo(action), jwt);

            // 不要将like插入actions中,这里依然线程不安全
            // publishLike()会填充actions[pos],导致对应的action的id不会空
            actions[pos] = new ActionPo();
        }

    }

}
