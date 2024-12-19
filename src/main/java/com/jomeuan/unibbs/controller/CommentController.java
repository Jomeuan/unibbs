package com.jomeuan.unibbs.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jomeuan.unibbs.entity.Action;
import com.jomeuan.unibbs.entity.Comment;
import com.jomeuan.unibbs.entity.domain.PostDo;
import com.jomeuan.unibbs.mapper.ActionMapper;
import com.jomeuan.unibbs.mapper.CommentMapper;
import com.jomeuan.unibbs.service.IActionService;
import com.jomeuan.unibbs.service.ICommentService;
import com.jomeuan.unibbs.valid.SortRule;
import com.jomeuan.unibbs.vo.R;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jomeuan
 * @since 2024-12-03
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private IActionService actionService;
    @Autowired
    private ActionMapper actionMapper;
    @Autowired
    private ICommentService commentService;
    @Autowired
    private CommentMapper commentMapper;

    /**
     * 根据 actionid 找到所有评论 id 为 actionId 的评论
     * 
     * @param actionId 被评论的action的id
     * @param sortRule
     * @return
     */
    @GetMapping("target/{actionId}")
    public R<List<PostDo>> getCommentByActionId(@PathVariable("actionId") Long actionId,
            @RequestParam("sortRule") String sortRule) {

        // TODO: 用spring的全局异常处理
        SortRule sr;
        try {
            sr = SortRule.parseSortRule(sortRule);
        } catch (RuntimeException e) {
            return R.error(e.getMessage());
        }

        // 根据actionId查找action对应的所有评论comment
        List<PostDo> posts = actionMapper.selectPostByActionId(actionId, sr.getKey().toString(), sr.isAsc());

        if (posts != null && posts.size() > 0) {
            return R.ok(posts);
        } else {
            return R.error("no comments found");
        }
    }

    @GetMapping("content/{contentId}")
    public R<List<PostDo>> getCommentByContentId(@PathVariable("contentId") Long contentId) {
        Comment comment = commentMapper.selectById(contentId);

        PostDo post = new PostDo();
        post.setContentId(contentId);
        post.setContent(comment.getContent());
        post.setLikesCount(comment.getLikesCount());
        post.setCollectionsCount(comment.getCollectionsCount());
        post.setCommentsCount(comment.getCommentsCount());
        post.setPullCount(comment.getPullCount());

        return R.ok(List.of(post));
    }

    /**
     * 查找 id为userId的用户 的所有Comment
     * 
     * @param userId
     * @param sortRule
     * @return
     */
    @GetMapping("user/{userId}")
    public R<List<PostDo>> getCommentByUserId(@PathVariable("userId") Long userId) {
        List<Action> actions = actionMapper.selectList(new QueryWrapper<Action>().eq("id", userId));
        List<Long> commentsIdList = actions.stream().map(action -> action.getContentId()).collect(Collectors.toList());
        List<Comment> comments = commentMapper.selectList(new QueryWrapper<Comment>().in("id", commentsIdList));
        
        return R.ok(List.of());
    }
}
