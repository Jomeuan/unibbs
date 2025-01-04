package com.jomeuan.unibbs.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jomeuan.unibbs.bo.PostBo;
import com.jomeuan.unibbs.entity.Action;
import com.jomeuan.unibbs.entity.Comment;
import com.jomeuan.unibbs.entity.domain.PostDo;
import com.jomeuan.unibbs.mapper.ActionMapper;
import com.jomeuan.unibbs.mapper.CommentMapper;
import com.jomeuan.unibbs.service.ICommentService;
import com.jomeuan.unibbs.service.IPostService;
import com.jomeuan.unibbs.util.IdGenerator;

@Service
public class PostServiceImpl implements IPostService {
    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private ActionMapper actionMapper;

    @Autowired
    private CommentMapper commentMapper;

    /**
     * @param post post.actionId不为空,contentId不为空
     */
    @Override
    public PostBo fillContent(PostBo post, Long contentId) {
        if (post.getActionId() == null || contentId == null) {
            throw new IllegalArgumentException("post.actionId and contentId must not be null");
        }
        if (post.getType() == Action.ActionType.COMMENT) {
            Comment comment = commentMapper.selectById(contentId);
            post.setContent(comment.getContent());
            post.setLikesCount(comment.getLikesCount());
            post.setCommentsCount(comment.getCommentsCount());
            post.setCollectionsCount(comment.getCollectionsCount());
            post.setPullCount(comment.getPullCount());
        }
        return post;
    }

    /**
     * @param post post.actionId 不为空, targetActionId 不为空
     */
    @Override
    public PostBo fillTargetContent(PostBo post, Long targetActionId) {
        if (post.getActionId() == null || targetActionId == null) {
            throw new IllegalArgumentException("post.actionId and contentId must not be null");
        }
        // 如果是评论(不是发文则填充评论对象的content)
        if (targetActionId != 1) {
            Long targetContentId = actionMapper.selectById(targetActionId).getContentId();
            Comment comment = commentMapper.selectById(targetContentId);
            post.setContent(comment.getContent());
            post.setLikesCount(comment.getLikesCount());
            post.setCollectionsCount(comment.getCollectionsCount());
            post.setCollectionsCount(comment.getCollectionsCount());
            post.setPullCount(comment.getPullCount());
        }
        return post;
    }

    @Override
    public List<PostBo> listCommentsOf(PostBo post) {
        // 找到targetId为post.getAction的action,再根据action构建Post,而后装填content
        List<Action> actions = actionMapper.selectList(new QueryWrapper<Action>().eq("target_id", post.getActionId())
                .eq("type", Action.ActionType.COMMENT.getValue()));
        List<PostBo> res = actions.stream().map(action -> new PostBo(action)).collect(Collectors.toList());
        res.stream().forEach(p -> this.fillContent(p, p.getContentId()));
        return res;
    }

    @Override
    public List<PostBo> listPostsByUserId(Long userId) {
        List<Action> actions = actionMapper.selectList(new QueryWrapper<Action>().eq("user_id", userId));
        List<PostBo> posts = actions.stream().map((action) -> {
            PostBo post = new PostBo(action);
            post.setContent(commentMapper.selectById(action.getContentId()).getContent());
            if (post.getTargetId() != 1) {
                Long targetContentId = actionMapper.selectById(action.getTargetId()).getContentId();
                post.setTargetContent(commentMapper.selectById(targetContentId).getContent());
            }
            return post;
        }).collect(Collectors.toList());
        return posts;
    }

    @Override
    public PostBo save(PostBo post) {
        if (post.getTime() == null)
            post.setTime(LocalDateTime.now());

        switch (post.getType()) {
            case Action.ActionType.COMMENT:
                return saveComment(post);
            case Action.ActionType.LIKE:
                return saveLike(post);
            default:
                throw new IllegalArgumentException("Unsupported post type");
        }
    }

    /**
     * 
     * @param post (未声明属性的为空)
     *             userId;
     *             type=ActionType.COMMENT=1
     *             time=前端传来的值或者insertPost中被赋值now();
     *             content 评论的内容
     *             targetId 如果是1则是发文,否则被评论的action的id
     * 
     * @return post 相较于入参post多了actionId和contentId字段
     */
    public PostBo saveComment(PostBo post) {
        if (post.getActionId() == null)
            post.setActionId(idGenerator.nextId().longValue());

        // 插入comment
        Comment comment = new Comment();
        Long commentId = idGenerator.nextId();
        comment.setId(commentId);
        comment.setContent(post.getContent());
        commentMapper.insert(comment);

        // 插入action
        post.setContentId(commentId);

        actionMapper.insert(post.toPostDo().getAction());

        // 如果是评论,要给评论对象的commentsCount+=1
        if (post.getTargetId() != null && post.getTargetId() != Action.ActionType.SUPER_COMMENT.getValue()) {
            // 被评论的action的id为post.getTargetId()
            Action actionCommented = actionMapper.selectById(post.getTargetId());

            // 被评论的comment的id为
            Long commentIdCommented = actionCommented.getContentId();

            // 被评论的comment的comments_count+=1
            commentService.update(
                    new UpdateWrapper<Comment>()
                            .eq("id", commentIdCommented)
                            .setIncrBy("comments_count", 1));
        }
        return post;

    }

    /**
     * @param post (未声明属性的为空)
     *             userId;
     *             type=Action.LIKE=3
     *             time=前端传来的值或者insertPost中被赋值now();
     *             contentId = (特别的指)被点赞的内容的id(如果被点赞的action没有内容为空)
     *             content "like"(点赞) "cancel"(取消点赞)
     *             targetId被点赞的action的id;
     * @return post 相较于入参post多了actionId字段
     */
    public PostBo saveLike(PostBo post) {
        // 判断action是否有过记录
        Action likeAction = actionMapper.selectOne(
                new QueryWrapper<Action>().eq("user_id", post.getUserId()).eq("target_id", post.getTargetId()));
        // 没有则新增一条
        if (likeAction == null) {
            if (post.getContent().equals("cancel"))
                throw new IllegalArgumentException("can not cannel a like doesn't exits check argument of'conent' ");
            post.setActionId(idGenerator.nextId());
            likeAction = post.toPostDo().getAction();
            actionMapper.insert(likeAction);
        }
        // 有则contentId+=1
        else {
            likeAction.setContentId(likeAction.getContentId() + 1);
            actionMapper.update(likeAction, new UpdateWrapper<Action>().eq("id", likeAction.getId()));
        }
        // 内容的like_count+=1或者-=1
        if (post.getContentId() != null) {
            Integer added = post.getContent().equals("like") ? 1 : post.getContent().equals("cancel") ? -1 : 0;
            commentMapper.updateLikeCount(post.getContentId(), added);
        }

        return new PostBo(likeAction);
    }

}
