package com.jomeuan.unibbs.forum.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jomeuan.unibbs.forum.domain.ActionType;
import com.jomeuan.unibbs.forum.domain.CommunityDo;
import com.jomeuan.unibbs.forum.domain.Roles;
import com.jomeuan.unibbs.forum.entity.ActionPo;
import com.jomeuan.unibbs.forum.entity.CommunityContentPo;
import com.jomeuan.unibbs.forum.entity.ModeratorPo;
import com.jomeuan.unibbs.forum.entity.ProfilePo;
import com.jomeuan.unibbs.forum.feign.ProfileFeignClient;
import com.jomeuan.unibbs.forum.mapper.ActionMapper;
import com.jomeuan.unibbs.forum.mapper.CommunityContentMapper;
import com.jomeuan.unibbs.forum.mapper.ModeratorMapper;
import com.jomeuan.unibbs.forum.mapper.PostMapper;
import com.jomeuan.unibbs.forum.service.CommunityService;
import com.jomeuan.unibbs.forum.service.JWTService;
import com.jomeuan.unibbs.forum.service.PostService;
import com.jomeuan.unibbs.forum.util.IdGenerator;
import com.jomeuan.unibbs.forum.vo.CommunityDetailVo;
import com.jomeuan.unibbs.forum.vo.ModeratorVo;
import com.jomeuan.unibbs.forum.vo.PostVo;
import com.jomeuan.unibbs.forum.vo.R;

import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@RestController
@RequestMapping("community")
public class CommunityController {
    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private JWTService jwtService;
    @Autowired
    private CommunityService communityService;
    @Autowired
    private PostService postService;

    @Autowired
    private CommunityContentMapper communityContentMapper;
    @Autowired
    private ModeratorMapper moderatorMapper;
    @Autowired
    private ActionMapper actionMapper;
    @Autowired
    private PostMapper postMapper;

    @Autowired
    private ProfileFeignClient profileFeignClient;

    @Transactional
    @Secured(Roles.VISITOR_ROLE_NAME)
    @PostMapping("")
    public Object newCommunity(@RequestBody CommunityDo communityDo, @RequestHeader("token") String jwt) {
        try {
            Assert.notNull(communityDo.getCommunityContent(), "communityContent cannot be null");
            Assert.isTrue(communityDo.getAction().getUserId().equals(jwtService.parseJWT(jwt).getUser().getId()),
                    "jwt not equal to user");
        } catch (IllegalArgumentException | NullPointerException e) {
            return R.error(e.getMessage());
        }
        Long communityId = idGenerator.nextId();
        // 1.预处理 commuintyContent
        CommunityContentPo communityContentPo = communityDo.getCommunityContent();
        communityContentPo.setId(communityId);

        // 2.预处理 action
        ActionPo action = communityDo.getAction();
        action.setId(communityId);
        action.setTime(LocalDateTime.now());
        action.setType(ActionType.COMMUNITY);
        action.setContentId(communityId);

        // 3.添加创建人到 moderators
        communityDo.setModerators(new ArrayList<>());
        List<ModeratorVo> moderators = communityDo.getModerators();
        moderators.add(new ModeratorVo(
                new ModeratorPo(idGenerator.nextId(), communityId, action.getUserId(), "CREATOR"),
                null));
        try {
            communityDo = communityService.newCommunity(communityDo);
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
            return R.error(e.getMessage());
        }
        return R.ok(communityDo);
    }

    @Secured(Roles.MODERATOR_ROLE_NAME)
    @Transactional
    @PutMapping("")
    public Object modifyCommunityContent(@RequestBody CommunityDo communityDo,
            @RequestHeader("token") String jwt) {

        Long commuityId;

        try {
            Assert.isTrue(communityDo.getAction().getUserId().equals(jwtService.parseJWT(jwt).getUser().getId()),
                    "jwt not equal to user");
            commuityId = communityDo.getAction().getTargetId();
            // 不能是null,对应的action_type是不是COMMUNITY
            communityService.checkCommunityId(commuityId);
            // 验证用户是不是该community的MODERATOR
            communityService.checkModerator(communityDo.getAction().getUserId(), commuityId);
        } catch (IllegalArgumentException | NullPointerException e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }

        // update community content
        communityContentMapper.updateById(communityDo.getCommunityContent());
        return R.ok(this.getCommunityDetail(commuityId, 1, 0));
    }

    /**
     * 返回板块的基本信息 和 板块下的一些帖子
     * 
     * @param communityId
     * @return
     */
    @GetMapping("index")
    public Object getCommunityDetail(@RequestParam Long communityId,
            @RequestParam Integer page, @RequestParam Integer limit) {
        // 验证communityID
        CommunityDetailVo res = new CommunityDetailVo();
        res.setPage(page);
        res.setLimit(limit);
        res.setCommunityContent(communityContentMapper.selectById(communityId));
        // 版主不分先后
        res.setModerators(moderatorMapper
                .selectList(Wrappers.lambdaQuery(ModeratorPo.class)
                        .eq(ModeratorPo::getCommunityId, communityId))
                .stream()
                .map(moderator -> new ModeratorVo(moderator,
                        profileFeignClient.getProfile(moderator.getUserId())))
                .collect(Collectors.toList()));
        // 按发布时间排序
        // List<ActionPo>->List<PostVo>
        res.setPosts(
                actionMapper.selectPage(new Page<>(page, limit), Wrappers.lambdaQuery(ActionPo.class)
                        .eq(ActionPo::getType, ActionType.COMMENT_BROADCAST)
                        .eq(ActionPo::getTargetId, communityId)
                        .orderBy(true, false, ActionPo::getTime)).getRecords()
                        // 转变成PostVo
                        .stream()
                        .map(action -> postService.getPostVoByActionId(action.getId()))
                        .collect(Collectors.toList()));
        return R.ok(res);
    }

    @Secured(Roles.MODERATOR_ROLE_NAME)
    @PostMapping("moderator")
    public Object addModerator(@RequestBody CommunityDo communityDo, @RequestHeader("token") String jwt) {
        Long commuityId;
        List<ModeratorVo> moderators;
        try {
            Assert.isTrue(communityDo.getAction().getUserId().equals(jwtService.parseJWT(jwt).getUser().getId()),
                    "jwt not equal to user");
            Assert.notEmpty(communityDo.getModerators(), "moderators can not be null");
            commuityId = communityDo.getAction().getTargetId();
            // 不能是null,对应的action_type是不是COMMUNITY
            communityService.checkCommunityId(commuityId);
            // 验证用户是不是该community的MODERATOR
            communityService.checkModerator(communityDo.getAction().getUserId(), commuityId);

            // 确保要添加的moderators不是这个community的moderator
            moderators = communityDo.getModerators();
            moderators.forEach(moderatorVo -> {
                if (moderatorMapper.selectCount(Wrappers.lambdaQuery(ModeratorPo.class)
                        .eq(ModeratorPo::getUserId, moderatorVo.getModerator().getUserId())
                        .eq(ModeratorPo::getCommunityId, commuityId)) > 0) {
                    throw new IllegalArgumentException("moderator already exists");
                }
            });
        } catch (IllegalArgumentException | NullPointerException e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
        // 预处理List<ModeratorVo> moderators;
        moderators.forEach(moderatorVo -> {
            moderatorVo.getModerator().setId(idGenerator.nextId());
            moderatorVo.getModerator().setCommunityId(commuityId);
        });
        communityService.addModerator(communityDo);
        return R.ok(this.getCommunityDetail(commuityId, 1, 0));
    }

    @Secured(Roles.MODERATOR_ROLE_NAME)
    @DeleteMapping("moderator")
    public Object removeModerator(@RequestBody CommunityDo communityDo, @RequestHeader("token") String jwt) {
        Long commuityId;
        List<ModeratorVo> moderators;
        try {
            Assert.isTrue(communityDo.getAction().getUserId().equals(jwtService.parseJWT(jwt).getUser().getId()),
                    "jwt not equal to user");
            Assert.notEmpty(communityDo.getModerators(), "moderators can not be null");
            commuityId = communityDo.getAction().getTargetId();
            // 不能是null,对应的action_type是不是COMMUNITY
            communityService.checkCommunityId(commuityId);
            // 验证用户是不是该community的MODERATOR
            communityService.checkModerator(communityDo.getAction().getUserId(), commuityId);

            // 确保要添加的moderators是这个community的moderator
            // 同时装填id
            moderators = communityDo.getModerators();
            moderators.forEach(moderatorVo -> {
                ModeratorPo moderatorPo = moderatorMapper.selectOne(Wrappers.lambdaQuery(ModeratorPo.class)
                        .eq(ModeratorPo::getUserId, moderatorVo.getModerator().getUserId())
                        .eq(ModeratorPo::getCommunityId, commuityId));
                if (moderatorPo == null) {
                    throw new IllegalArgumentException("moderator no exists");
                }
                moderatorVo.setModerator(moderatorPo);
            });
        } catch (IllegalArgumentException | NullPointerException e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
        communityService.removeModerator(communityDo);
        return R.ok(this.getCommunityDetail(commuityId, 1, 0));
    }
}
