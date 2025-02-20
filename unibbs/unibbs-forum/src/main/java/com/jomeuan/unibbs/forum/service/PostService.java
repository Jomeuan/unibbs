package com.jomeuan.unibbs.forum.service;

import java.time.Duration;
import java.time.LocalDateTime;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jomeuan.unibbs.domain.ActionType;
import com.jomeuan.unibbs.domain.PostDo;
import com.jomeuan.unibbs.entity.ActionPo;
import com.jomeuan.unibbs.entity.CommentPo;
import com.jomeuan.unibbs.event.PostPublishEvent;
import com.jomeuan.unibbs.forum.mapper.ActionMapper;
import com.jomeuan.unibbs.forum.mapper.CommentMapper;
import com.jomeuan.unibbs.util.IdGenerator;
import com.jomeuan.unibbs.vo.PostVo;

@Service
public class PostService {

    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private ActionMapper actionMapper;
    @Autowired
    private CommentMapper commentMapper;

    // TODO: 从redis 缓存中取
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
        if (res.getThisPost().getAction().getTargetId() != null) {
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

    /**
     * 获取actionId对应根评论
     * 
     * @param actionId
     * @return
     */
    public PostDo getRootPost(Long actionId) {

        ActionPo actionPo = actionMapper.selectById(actionId);

        if (actionPo.getType() == ActionType.COMMENT) {
            return this.getRootPost(actionPo.getTargetId());
        } else if (actionPo.getType() == ActionType.COMMENT_BROADCAST) {
            return this.getPostDoByActionId(actionPo.getId());
        } else {
            throw new IllegalArgumentException(
                    "post type should be ActionType.COMMENT or ActionType.COMMENT_BROADCAST");
        }

    }

    /**
     * 
     * @param postDo
     */
    @Transactional
    public PostDo savePost(PostDo postDo) {
        ActionPo action = postDo.getAction();
        CommentPo comment = postDo.getComment();
        comment.setId(idGenerator.nextId());
        // 插入content
        commentMapper.insert(comment);
        action.setContentId(comment.getId());
        action.setId(idGenerator.nextId());
        action.setTime(LocalDateTime.now());

        ActionPo targetAction = actionMapper.selectById(action.getTargetId());

        if (action.getType() == ActionType.COMMENT) {
            // 如果post.type是Comment,targetPost.type 为 COMMENT 或者 COMMENT_BROADCAST
            Assert.isTrue(
                    targetAction.getType() == ActionType.COMMENT
                            || targetAction.getType() == ActionType.COMMENT_BROADCAST,
                    "target action type error with type " + targetAction.getType());
            actionMapper.insert(action);
            // 如果是COMMENT,要给评论对象的commentsCount+=1
            commentMapper.update(Wrappers.lambdaUpdate(CommentPo.class)
                    .eq(CommentPo::getId, targetAction.getContentId()).setIncrBy(CommentPo::getCommentsCount, 1));

        } else if (action.getType() == ActionType.COMMENT_BROADCAST) {
            // 如果post.type是COMMENT_BROADCAST,targetPost.type 为 COMUNITY
            Assert.isTrue(targetAction.getType() == ActionType.COMMUNITY,
                    "targetAction.type error with type" + targetAction.getType());

            action.setType(ActionType.COMMENT_BROADCAST);
            actionMapper.insert(action);

        } else
            throw new IllegalArgumentException("target Action type " + targetAction.getType() + " is not supported");

        return postDo;
    }

    /**
     * 缓存post
     * 
     * @param event
     * @return
     */
    @EventListener()
    public PostDo putPostCache(PostPublishEvent event) {
        // BoundZSetOperations<Object,Object> zset =
        // redisTemplate.boundZSetOps("rankings");
        PostDo postDo = event.getPostDo();
        RBucket<Object> bucket = redissonClient
                .getBucket(postDo.getAction().getType().toString() + ":" + postDo.getAction().getId());

        // 缓存post
        // bucket.set(postDo, Duration.ofHours(1));
        bucket.set(postDo, Duration.ofMinutes(1));

        return postDo;
    }

    /**
     * 更新rankings
     * 
     * @param event
     * @return
     */
    @EventListener()
    public PostDo updateRankings(PostPublishEvent event) {
        PostDo postDo = event.getPostDo();
        // RSortedSet<PostDo> sortedSet = redissonClient.getSortedSet("rankings");;
        BoundZSetOperations<Object,Object> zset =
            redisTemplate.boundZSetOps("rankings");

        PostDo rootPostDo = this.getRootPost(postDo.getAction().getId());
        
        zset.addIfAbsent(rootPostDo.getAction().getId(),0);
        zset.incrementScore(rootPostDo.getAction().getId(), 1);
        
        return postDo;
    }

}
