package com.jomeuan.unibbs.forum.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jomeuan.unibbs.forum.domain.ActionType;
import com.jomeuan.unibbs.forum.domain.PostDo;
import com.jomeuan.unibbs.forum.entity.ActionPo;
import com.jomeuan.unibbs.forum.entity.CommentPo;
import com.jomeuan.unibbs.forum.mapper.ActionMapper;
import com.jomeuan.unibbs.forum.mapper.CommentMapper;
import com.jomeuan.unibbs.forum.util.IdGenerator;
import com.jomeuan.unibbs.forum.vo.PostVo;

@Service
public class PostService {

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private ActionMapper actionMapper;

    @Autowired
    private CommentMapper commentMapper;

    public PostDo getPostDoByActionId(Long actionId) {
        PostDo res = new PostDo();
        res.setAction(actionMapper.selectById(actionId));
        // 只有action_type为下面值时才装填comment
        switch (res.getAction().getType()) {
            case ActionType.COMMENT:
            case ActionType.COMMENT_BROADCAST:
                res.setComment(commentMapper.selectById(res.getAction().getContentId()));
            default:
                break;
        }
        return res;
    }

    public PostVo getPostVoByActionId(Long actionId) {

        PostVo res = new PostVo(this.getPostDoByActionId(actionId), null);
        // 管理删除的commonet 会出现targetId==null 的情况
        if (res.getThisPost().getAction().getTargetId()!=null) {
            switch (res.getThisPost().getAction().getType()) {
                case ActionType.COMMENT:
                case ActionType.LIKE:
                    res.setTargetPost(this.getPostDoByActionId(res.getThisPost().getAction().getTargetId()));
                default:
                    break;
            }
        }
        return res;
    }

}
