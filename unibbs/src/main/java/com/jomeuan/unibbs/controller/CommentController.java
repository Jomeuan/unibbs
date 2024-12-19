package com.jomeuan.unibbs.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jomeuan.unibbs.entity.Action;
import com.jomeuan.unibbs.entity.Comment;
import com.jomeuan.unibbs.mapper.ActionMapper;
import com.jomeuan.unibbs.service.IActionService;
import com.jomeuan.unibbs.service.ICommentService;
import com.jomeuan.unibbs.service.impl.ActionServiceImpl;
import com.jomeuan.unibbs.vo.Post;
import com.jomeuan.unibbs.vo.R;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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

    public static final int DEFAULT_PAGE_SIZE = 20;

    @Autowired
    private IActionService actionService;
    @Autowired
    private ICommentService commentService;

    /**
     * 根据 targetId 查找action对应的所有评论comment
     * targetId不为0时向用户targetId的comment
     * 
     * @param sortRule key_order:key包括(time,likes,collects,comments),order包括(asc,desc);
     *                 例如time_desc按发表时间晚早排序,likes_asc按照点赞数少多排序, // TODO :目前仅支持单一key
     *  
     * @return R
     */
    @GetMapping("{actionId}")
    public R<List<Post>> getComment(@PathVariable("targetId") Long targetId, @RequestParam("page") Integer pageIndex,
            @RequestParam("sortRule") String sortRule) {

        //TODO: 封装成函数:验证sortRule
        String[] sortRuleTokens = sortRule.split("_");
        if (sortRuleTokens.length != 2) {
            return R.error("sortRule参数不合法");
        }
        String key = sortRuleTokens[0].toLowerCase();
        String order = sortRuleTokens[1].toLowerCase();


        List<Post> posts = actionMapper.selectPostByActionId(targetId, key, order.equals("asc"));

        if (posts != null && posts.size() > 0){
            return R.ok(posts);
        }else return R.error("no comments found");
    }

    /**
     * 用户发表评论
     * 
     * @param post 如果是发文则targetId为0;如果是评论要指出targetId和其对应的contentId
     * @return
     */
    @PostMapping()
    public R<?> postComment(@RequestBody Post post) {
        actionService.insertPost(post);
        return R.ok(null);
    }

    /**
     * 查找 id为userId的用户 的所有Comment
     * 
     * @param userId
     * @param sortRule
     * @return
     */
    @PutMapping("{commentId}")
    public R putMethodName(@PathVariable Long commentId, @RequestBody Post post) {
        throw new RuntimeException("undo");
    }

    public static void main(String[] args) {
        System.out.println(LocalDateTime.now());
    }
}
