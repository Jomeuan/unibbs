package com.jomeuan.unibbs.forum.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jomeuan.unibbs.domain.ActionType;
import com.jomeuan.unibbs.domain.LikeDo;
import com.jomeuan.unibbs.domain.PostDo;
import com.jomeuan.unibbs.domain.Roles;
import com.jomeuan.unibbs.entity.ActionPo;
import com.jomeuan.unibbs.entity.CommentPo;
import com.jomeuan.unibbs.forum.mapper.ActionMapper;
import com.jomeuan.unibbs.forum.mapper.CommentMapper;
import com.jomeuan.unibbs.forum.mapper.CommunityContentMapper;
import com.jomeuan.unibbs.forum.mapper.ModeratorMapper;
import com.jomeuan.unibbs.forum.mapper.PostMapper;
import com.jomeuan.unibbs.forum.service.CommunityService;
import com.jomeuan.unibbs.forum.service.PostService;
import com.jomeuan.unibbs.util.IdGenerator;
import com.jomeuan.unibbs.util.JWTService;
import com.jomeuan.unibbs.vo.PostDetailVo;
import com.jomeuan.unibbs.vo.PostVo;
import com.jomeuan.unibbs.vo.R;

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
    private IdGenerator idGenerator;

    @Autowired
    private JWTService jwtService;
    @Autowired
    private PostService postService;

    @Autowired
    private ActionMapper actionMapper;
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    CommunityService communityService;

    /**
     * 用户发文/评论
     * 
     * @return PostVo with postDo and targetPostDo
     */
    @Secured(Roles.VISITOR_ROLE_NAME)
    @Transactional
    @PostMapping("")
    public Object publishPost(@RequestBody PostDo post, @RequestHeader("token") String jwt) {
        try {
            Assert.notNull(post.getComment(), "comment cannot be null");
            Assert.notNull(post.getAction().getTargetId(), "TargetId cannot be null");
            Assert.isTrue(post.getAction().getUserId().equals(jwtService.parseJWT(jwt).getUser().getId()),
                    "jwt not equal to user");
            Assert.isTrue(
                    post.getAction().getType() == ActionType.COMMENT
                            || post.getAction().getType() == ActionType.COMMENT_BROADCAST,
                    "action type should be 1 or 6");
        } catch (IllegalArgumentException | NullPointerException e) {
            return R.error(e.getMessage());
        }

        ActionPo action = post.getAction();
        CommentPo comment = post.getComment();
        comment.setId(idGenerator.nextId());
        // 插入content
        commentMapper.insert(comment);
        action.setContentId(comment.getId());
        action.setId(idGenerator.nextId());
        action.setTime(LocalDateTime.now());

        ActionPo targetAction = actionMapper.selectById(action.getTargetId());

        // 判断actionType :COMMENT_BROADCAST/COMMENT
        if (targetAction.getType() == ActionType.COMMENT || targetAction.getType() == ActionType.COMMENT_BROADCAST) {
            action.setType(ActionType.COMMENT);
            actionMapper.insert(action);
            // 如果是COMMENT,要给评论对象的commentsCount+=1
            commentMapper.update(Wrappers.lambdaUpdate(CommentPo.class)
                    .eq(CommentPo::getId, targetAction.getContentId()).setIncrBy(CommentPo::getCommentsCount, 1));
        } else if (targetAction.getType() == ActionType.COMMUNITY) {
            action.setType(ActionType.COMMENT_BROADCAST);
            actionMapper.insert(action);
        } else
            throw new IllegalArgumentException("target Action type " + targetAction.getType() + " is not supported");

        PostVo res = new PostVo();
        res.setThisPost(post);
        res.setTargetPost(new PostDo(targetAction, commentMapper.selectById(targetAction.getContentId())));

        return R.ok(res);
    }

    /**
     * 用户点赞/取消点赞
     * 
     * @param likePubulishVo {@link com.jomeuan.unibbs.vo.LikePubulishVo}
     * @return
     */
    @Secured(Roles.VISITOR_ROLE_NAME)
    @Transactional
    @PostMapping("/like")
    public Object publishLike(@RequestBody LikeDo likeDo, @RequestHeader("token") String jwt) {

        try {
            Assert.notNull(likeDo.getAction().getTargetId(), "likeDo.action.targetId cannot be null");
            Assert.isTrue(likeDo.getAction().getUserId().equals(jwtService.parseJWT(jwt).getUser().getId()),
                    "jwt not equal to user");
            Assert.isTrue(likeDo.getAction().getType().equals(ActionType.LIKE), jwt);
            Assert.isTrue(Math.abs(likeDo.getAction().getContentId()) == 1,
                    "contentId should be 1(like) or -1(cancel like)");
        } catch (IllegalArgumentException | NullPointerException e) {
            return R.error(e.getMessage());
        }

        // 插入或者修改likeAction
        ActionPo likeAction = actionMapper.selectOne(
                Wrappers.lambdaQuery(ActionPo.class)
                        .eq(ActionPo::getTargetId, likeDo.getAction().getTargetId())
                        .eq(ActionPo::getUserId, likeDo.getAction().getUserId())
                        .eq(ActionPo::getType, ActionType.LIKE));
        if (likeAction == null) {
            if (likeDo.getAction().getContentId() != 1) {
                return R.error(" likeDo.action.contentId should be 1(like) because the user never LIKE this comment ");
            }
            likeAction = likeDo.setLikesCount(1).getAction();
            likeAction.setId(idGenerator.nextId());
            likeAction.setTime(LocalDateTime.now());
            actionMapper.insert(likeAction);
        } else if (likeAction != null) {
            // 无视likeDo中的content_id是1还是-1
            likeAction.setContentId(likeAction.getContentId() + 1);
            likeAction.setTime(LocalDateTime.now());
            actionMapper.updateById(likeAction);
            likeDo.setAction(likeAction);
        }

        // 给targetContent.likeCounts+=1/-=1: contentId=1
        Long targetContentId = actionMapper.selectById(likeDo.getAction().getTargetId()).getContentId();
        commentMapper.update(Wrappers.lambdaUpdate(CommentPo.class).eq(CommentPo::getId, targetContentId)
                .setIncrBy(CommentPo::getLikesCount, (likeAction.getContentId() % 2 == 0) ? -1 : 1));
        return R.ok(likeDo);
    }

    /**
     * 获取actionId对应的帖子及其评论
     * 在评论中 二级评论只显示 op(楼主) 和 评论人的
     * TODO: 评论的order
     */
    @GetMapping("detail")
    public R<PostDetailVo> getPostDetail(@RequestParam Long actionId) {
        try {
            Assert.notNull(actionId, "actionId cannot be null");
        } catch (IllegalArgumentException | NullPointerException e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }

        PostVo thisPost = postService.getPostVoByActionId(actionId);

        PostDetailVo res = new PostDetailVo();
        // 1.填充orignal(楼主的帖子) res.postVo
        res.setPost(thisPost);
        // 2.填充评论
        res.setComments(new ArrayList<>());
        // 2.1获取评论的action_id
        List<ActionPo> commentActionPoList = actionMapper.selectList(Wrappers.lambdaQuery(ActionPo.class)
                .eq(ActionPo::getTargetId, actionId).eq(ActionPo::getType, ActionType.COMMENT));
        for (ActionPo commentActionPo : commentActionPoList) {
            // 一级评论
            PostDetailVo comment = new PostDetailVo();
            // 2.2填充评论的 postvo,注意不填充targetPost
            comment.setPost(new PostVo(postService.getPostDoByActionId(commentActionPo.getId()), new PostDo()));
            // 2.3填充评论的评论
            // 2.3.1 获得二级评论的action_id(user_id=楼主/一级评论的作者)
            List<ActionPo> comment2ActionPo = actionMapper.selectList(
                    Wrappers.lambdaQuery(ActionPo.class)
                            .eq(ActionPo::getTargetId, comment.getPost().getThisPost().getAction().getId())
                            .eq(ActionPo::getType, ActionType.COMMENT)
                            .in(ActionPo::getUserId,
                                    List.of(res.getPost().getThisPost().getAction().getUserId(),
                                            comment.getPost().getThisPost().getAction().getId())));

            // 2.3.2 填充二级评论到一级评论comment.coments:将二级评论的List<action>转变成List<PostDetailVo>
            comment.setComments(
                    comment2ActionPo.stream()
                            .map(action -> new PostDetailVo(
                                    new PostVo(postService.getPostDoByActionId(action.getId()), null), null))
                            .collect(Collectors.toList()));

            // 添加到评论
            res.getComments().add(comment);
        }
        return R.ok(res);
    }

    @GetMapping()
    public Object listPosts(@RequestParam Long userId, @RequestParam Integer page, @RequestParam Integer limit,
            @RequestHeader("token") String jwt) {
        try {
            Assert.isTrue(userId.equals(jwtService.parseJWT(jwt).getUser().getId()),
                    "jwt not equal to user");
        } catch (IllegalArgumentException | NullPointerException e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
        List<ActionPo> actions = actionMapper
                .selectPage(new Page<>(page, limit), Wrappers.lambdaQuery(ActionPo.class)
                        .eq(ActionPo::getUserId, userId)
                        .orderBy(true, false, ActionPo::getTime))
                .getRecords();
        List<PostVo> posts = actions.stream()
                .map(action -> postService.getPostVoByActionId(action.getId()))
                .collect(Collectors.toList());
        return R.ok(posts);
    }

    /**
     * 修改评论
     * 
     * @param post targetId 指向被修改的action
     * @return
     */
    @Secured(Roles.VISITOR_ROLE_NAME)
    @Transactional
    @PutMapping("")
    public R<?> modifyPost(@RequestBody PostDo post, @RequestHeader("token") String jwt) {
        try {
            Assert.hasText(post.getComment().getContent(), "comment content can not be empty");
            Assert.notNull(post.getAction().getTargetId(), jwt);
            Assert.isTrue(post.getAction().getUserId().equals(jwtService.parseJWT(jwt).getUser().getId()),
                    "jwt not equal to user");
        } catch (IllegalArgumentException | NullPointerException e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }

        ActionPo targetAction = actionMapper.selectById(post.getAction().getTargetId());
        // 校验被修改的评论是不是用户的,以及是不是COMMENT/COMMENT_BROADCAST
        try {
            Assert.isTrue(targetAction.getUserId().equals(post.getAction().getUserId()), "target comment not user");
            Assert.isTrue(
                    targetAction.getType() == ActionType.COMMENT
                            || targetAction.getType() == ActionType.COMMENT_BROADCAST,
                    "target action type should be COMMENT or COMMENT_BROADCAST");

        } catch (IllegalArgumentException | NullPointerException e) {
            return R.error(e.getMessage());
        }
        // 新插入一条comment,其点赞数量等信息要从数据库中取而不是前端的数据
        CommentPo targetComment = commentMapper.selectById(targetAction.getContentId());
        CommentPo newComment = new CommentPo(idGenerator.nextId(), post.getComment().getContent(),
                targetComment.getLikesCount(), targetComment.getCommentsCount(), targetComment.getCollectionsCount(),
                targetComment.getPullCount());
        commentMapper.insert(newComment);
        // .新插入一条action,其type为Action.COMMENT
        ActionPo newAction = new ActionPo(idGenerator.nextId(), post.getAction().getUserId(),
                targetAction.getTargetId(), targetAction.getType(), newComment.getId(), LocalDateTime.now());
        actionMapper.insert(newAction);
        // .把被修改的评论的type变成Action.COMMENT_COVERED=2,targetID 变成 最新有效的评论/发言的action_id
        targetAction.setType(ActionType.COMMENT_COVERED);
        targetAction.setTargetId(newAction.getId());
        actionMapper.updateById(targetAction);

        return R.ok(postService.getPostVoByActionId(newAction.getId()));
    }

    /**
     * 管理员从板块中删除评论
     */
    @Secured(Roles.MODERATOR_ROLE_NAME)
    @Transactional
    @DeleteMapping
    public Object deletePost(@RequestBody PostDo postDo, @RequestHeader("token") String jwt) {
        ActionPo postAction;
        try {
            ActionPo action = postDo.getAction();
            Assert.isTrue(action.getUserId().equals(jwtService.parseJWT(jwt).getUser().getId()),
                    "jwt not equal to user");
            Assert.notNull(action.getTargetId(), "target post Id cannot be null");

            Long commuityId = action.getContentId();
            // 不能是null,对应的action_type是不是COMMUNITY
            communityService.checkCommunityId(commuityId);
            // 验证用户是不是该community的MODERATOR
            communityService.checkModerator(action.getUserId(), commuityId);

            // 验证要删除的帖子是COMMENT(_BROADCAST)
            postAction = actionMapper.selectById(action.getTargetId());
            Assert.isTrue(
                    postAction.getType() == ActionType.COMMENT || postAction.getType() == ActionType.COMMENT_BROADCAST,
                    "target post type should be Comment or Comment_Broadcast");

        } catch (IllegalArgumentException | NullPointerException e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
        // 找到最target的post是不是指向community
        Long commuityId = postDo.getAction().getContentId();
        ActionPo tmpAction = postAction;
        while (tmpAction.getType() == ActionType.COMMENT) {
            tmpAction = actionMapper.selectById(postAction.getTargetId());
        }
        // 确定最target的post.Type是COMMENT_BROADCAST,并且对应的communityId相同
        if (tmpAction.getType() == ActionType.COMMENT_BROADCAST
                && actionMapper.selectById(tmpAction.getTargetId()).getId().equals(commuityId)) {
            // 修改action的target_id为null
            actionMapper.update(null, Wrappers.lambdaUpdate(ActionPo.class)
                    .set(ActionPo::getTargetId, null)
                    .eq(ActionPo::getId, postAction.getId()));
        } else {
            // 无权删除不属于你管理的帖子
            return R.error("You do not have the right to delete posts that you do not manage.");
        }
        return R.ok(null);
    }
}
