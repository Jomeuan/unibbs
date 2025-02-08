package com.jomeuan.unibbs.forum.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jomeuan.unibbs.forum.domain.ActionType;
import com.jomeuan.unibbs.forum.domain.CommunityDo;
import com.jomeuan.unibbs.forum.domain.Roles;
import com.jomeuan.unibbs.forum.domain.UserAuthentication;
import com.jomeuan.unibbs.forum.entity.ActionPo;
import com.jomeuan.unibbs.forum.entity.ModeratorPo;
import com.jomeuan.unibbs.forum.entity.UserPo;
import com.jomeuan.unibbs.forum.feign.SecurityFeignClient;
import com.jomeuan.unibbs.forum.mapper.ActionMapper;
import com.jomeuan.unibbs.forum.mapper.CommunityContentMapper;
import com.jomeuan.unibbs.forum.mapper.ModeratorMapper;
import com.jomeuan.unibbs.forum.mapper.PostMapper;
import com.jomeuan.unibbs.forum.vo.ModeratorVo;
import com.jomeuan.unibbs.forum.vo.R;

import io.seata.spring.annotation.GlobalTransactional;

@Service
public class CommunityService {
    @Autowired
    private CommunityContentMapper communityContentMapper;
    @Autowired
    private ModeratorMapper moderatorMapper;
    @Autowired
    private ActionMapper actionMapper;
    @Autowired
    private PostMapper postMapper;

    @Autowired
    private SecurityFeignClient securityFeignClient;

    /**
     * 插入communityDo 中的communityContent,action,给创建人变成Moderator
     * 
     * @param communityDo
     * @return
     */
    @GlobalTransactional
    public CommunityDo newCommunity(CommunityDo communityDo) {
        // 插入communityContent
        communityContentMapper.insert(communityDo.getCommunityContent());
        // 插入action
        actionMapper.insert(communityDo.getAction());
        // 给moderators变更角色
        for (ModeratorVo moderatorVo : communityDo.getModerators()) {
            // 添加到moderator表
            moderatorMapper.insert(moderatorVo.getModerator());
            // 变更角色
            UserPo userPo = new UserPo();
            userPo.setId(communityDo.getAction().getUserId());
            securityFeignClient.addRole(new UserAuthentication(userPo, List.of(Roles.MODERATOR_ROLE)));
        }
        return communityDo;
    }

    @GlobalTransactional
    public CommunityDo addModerator(CommunityDo communityDo) {

        List<CompletableFuture<Void>> cfList = communityDo.getModerators().stream().map(moderatorVo -> {
            // 添加到moderator表
            moderatorMapper.insert(moderatorVo.getModerator());
            // 变更角色
            UserPo userPo = new UserPo();
            userPo.setId(communityDo.getAction().getUserId());
            return CompletableFuture.runAsync(
                    () -> securityFeignClient.addRole(new UserAuthentication(userPo, List.of(Roles.MODERATOR_ROLE))));
        }).collect(Collectors.toList());
        return communityDo;
    }

    @GlobalTransactional
    public void removeModerator(CommunityDo communityDo) {
        List<Long> idList = communityDo.getModerators().stream()
                .map(moderatorVo -> moderatorVo.getModerator().getId())
                .collect(Collectors.toList());
        moderatorMapper.deleteByIds(idList);
    }

    /**
     * 验证userId 是不是 commutyId 对应的 moderator
     * 不保证coomunityId对应的Action_type 是 COMMUNITY
     * 不保证userId 是 MODERATOR
     * 
     * @param userId
     * @param communityId
     * @return
     */
    public void checkModerator(Long userId, Long communityId) throws IllegalArgumentException {
        ModeratorPo moderatorPo = moderatorMapper
                .selectOne(Wrappers.lambdaQuery(ModeratorPo.class)
                        .eq(ModeratorPo::getCommunityId, communityId)
                        .eq(ModeratorPo::getUserId, userId));
        // 验证用户是不是该community的MODERATOR
        Assert.notNull(moderatorPo,
                "user not target community moderator");
    }

    /**
     * 验证coomnityId是不是合法的:
     * 不能是null,是否存在,对应的action_type是不是COMMUNITY
     * 
     * @param communityId
     * @throws IllegalArgumentException
     */
    public void checkCommunityId(Long communityId) throws IllegalArgumentException {
        Assert.notNull(communityId, "communityId cannot be null");
        ActionPo communityAction = actionMapper.selectById(communityId);

        Assert.notNull(communityAction, "community Id not exist");
        // 验证用户是不是该community的MODERATOR
        Assert.isTrue(communityAction.getType().equals(ActionType.COMMUNITY),
                "targetAction type should be ActionType.COMMUNITY");
    }
}
