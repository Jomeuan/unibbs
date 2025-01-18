package com.jomeuan.unibbs.forum.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jomeuan.unibbs.forum.bo.PostBo;
import com.jomeuan.unibbs.forum.entity.Action;
import com.jomeuan.unibbs.forum.entity.domain.PostDo;
import com.jomeuan.unibbs.forum.mapper.ActionMapper;
import com.jomeuan.unibbs.forum.mapper.CommentMapper;
import com.jomeuan.unibbs.forum.service.IActionService;
import com.jomeuan.unibbs.forum.service.ICommentService;
import com.jomeuan.unibbs.forum.service.IPostService;
import com.jomeuan.unibbs.forum.vo.LikePubulishVo;
import com.jomeuan.unibbs.forum.vo.PostDetailVo;
import com.jomeuan.unibbs.forum.vo.PostVo;
import com.jomeuan.unibbs.forum.vo.R;



/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jomeuan
 * @since 2024-12-03
 */
@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private IActionService actionService;
    @Autowired
    private ActionMapper actionMapper;
    @Autowired
    private IPostService postService;
    @Autowired
    private ICommentService commentService;
    @Autowired
    private CommentMapper commentMapper;

    @GetMapping("")
    public R<List<PostVo>> getPosts(@RequestParam("userId") Long userId) {
        // 根据userId获得其发过的帖子
        if (userId != null) {
            List<PostVo> res = postService.listPostsByUserId(userId).stream()
                    .map(post -> new PostVo(post))
                    .collect(Collectors.toList());
            return R.ok(res);
        }
        // 推荐热门帖子;
        // TODO: 用redis实现?
        else {
            return R.error("no implemented");
        }
    }

    @GetMapping("detail")
    public R<PostDetailVo> getPostDetail(@RequestParam("actionId") Long actionId) {
        PostBo masterPost = new PostBo(actionMapper.selectById(actionId));
        masterPost = postService.fillContent(masterPost, masterPost.getContentId());
        masterPost = postService.fillTargetContent(masterPost, masterPost.getTargetId());

        PostDetailVo res = new PostDetailVo();
        res.setPostVo(new PostVo(masterPost));

        List<PostDetailVo.Comment> commentVos = new ArrayList<>();
        List<PostBo> commentPosts = postService.listCommentsOf(masterPost);
        for (PostBo commentPost : commentPosts) {
            PostDetailVo.Comment r = new PostDetailVo.Comment();
            // 装填评论的内容
            r.setPostVo(new PostVo(commentPost));

            // 获得该评论是否被贴主评论了
            Action masterCommentAction = actionMapper.selectOne(
                    new QueryWrapper<Action>()
                            .eq("user_id", masterPost.getUserId())
                            .eq("target_id", commentPost.getActionId()));
            if (masterCommentAction != null) {
                PostBo masterCommentPost = new PostBo(masterCommentAction);
                masterCommentPost = postService.fillContent(masterCommentPost, masterCommentPost.getContentId());
                // 装入r.comment
                r.setComment(new PostDetailVo.Comment(new PostVo(masterCommentPost), null));
            }
            // 没有贴主评论评论 的评论(帖子)
            else {
                r.setComment(null);
            }
            commentVos.add(r);
        }

        res.setComments(commentVos);

        return R.ok(res);
    }

    /**
     * 用户发文/评论
     * 
     * @param postVo {@link com.jomeuan.unibbs.service.impl.PostServiceImpl#saveComment(com.jomeuan.unibbs.bo.PostBo)}
     * @return
     */
    @PostMapping("/comment")
    public R<?> publishcomment(@RequestBody PostVo postVo) {
        postService.save(postVo.toPostBo());
        return R.ok(null);
    }

    /**
     * 用户点赞/取消点赞
     * 
     * @param likePubulishVo {@link com.jomeuan.unibbs.vo.LikePubulishVo}
     * @return
     */
    @PostMapping("/like")
    public R<?> publishLike(@RequestBody LikePubulishVo likePubulishVo) {
        postService.save(likePubulishVo.toPostBo());
        return R.ok(null);
    }

    /**
     * 修改评论
     * 
     * @param post
     *             actionId;userId;type;time;
     *             contentId指向
     * @return
     */
    @PutMapping("/comment")
    public R<?> putComment(@RequestBody PostDo post) {

        // 把被修改的评论的type变成Action.COMMENT_COVERED=2

        // 新插入一条comment,其lastCommentId指向原先的comment,其点赞数量等信息要从数据库中取而不是前端的数据

        // 新插入一条action,其type为Action.COMMENT
        return R.ok(null);
    }

}
