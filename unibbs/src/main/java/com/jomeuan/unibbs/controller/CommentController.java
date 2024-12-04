package com.jomeuan.unibbs.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jomeuan.unibbs.entity.Action;
import com.jomeuan.unibbs.entity.Comment;
import com.jomeuan.unibbs.service.IActionService;
import com.jomeuan.unibbs.service.ICommentService;
import com.jomeuan.unibbs.service.impl.ActionServiceImpl;
import com.jomeuan.unibbs.vo.CommentAction;
import com.jomeuan.unibbs.vo.R;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jomeuan
 * @since 2024-12-03
 */
@Controller
@RequestMapping("/comment")
public class CommentController {
    @Autowired()
    private IActionService actionService;
    @Autowired
    private ICommentService commentService;

    private IdentifierGenerator identifierGenerator = DefaultIdentifierGenerator.getInstance();

    /**
     * path : {targetId}
     * targetId为0时表示向用户推荐一页comment
     * targetId不为0时向用户targetId的comment
     * @param param 数字:指定出数目; "all":所有的或者尽量多的; 
     * @param sortRule 规则_顺序:规则包括(time,likes,collects,comments,length),顺序包括(ashhc,desc);
     *       例如time_desc按发表时间晚早排序,likes_asc按照点赞数少多排序
     * @return R 
     */
    @GetMapping("{targetId}")
    public R getComment(@PathVariable("targetId") Long targetId,@RequestParam("page") Integer requestPage,@RequestParam("sortRule") String sortRule) {
        // TODO 一页的大小
        actionService.page(new Page<>(requestPage,20),new QueryWrapper<>().eq("target_id", targetId).)
        return new String();
    }
    
    /**
     * 用户发表评论
     * @param commentAction 至少包含action.user_id,comment.content
     * @return
     */
    @PostMapping()
    public R postComment(@RequestBody CommentAction commentAction) {
        try {
            Comment comment = commentAction.getComment();
            Long commentId = identifierGenerator.nextId(comment).longValue();
            comment.setId(commentId);
            commentService.save(commentAction.getComment());

            Action action = commentAction.getAction();
            action.setContentId(commentId);
            if(action.getTime()==null)action.setTime(LocalDateTime.now());
            actionService.save(action);
        } catch (RuntimeException e) {
            // TODO 装填各种异常到R.msg
            e.printStackTrace();
        }
        return R.ok(null);
    }

    /**
     * 用户修改
     * @param commentId
     * @param commentAction
     * @return
     */
    @PutMapping("{commentId}")
    public R putMethodName(@PathVariable Long commentId, @RequestBody CommentAction commentAction) {
        Action action = commentAction.getAction();
        action.
        
        return entity;
    }

    public static void main(String[] args) {
        System.out.println(LocalDateTime.now());
    }

}
