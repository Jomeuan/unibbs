package com.jomeuan.unibbs.profile.controller;

import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jomeuan.unibbs.domain.Roles;
import com.jomeuan.unibbs.entity.ProfilePo;
import com.jomeuan.unibbs.profile.mapper.ProfileMapper;
import com.jomeuan.unibbs.vo.R;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileMapper profileMapper;
    
    @GetMapping()
    public Object getProfile(@RequestParam(required = false) Long userId) {
        try {
            Assert.notNull(userId, "userId cannot be null");
        } catch (IllegalArgumentException | NullPointerException e) {
            return R.error(e.getMessage());
        }
        return ResponseEntity.ok(profileMapper.selectById(userId));
    }

    @Secured(Roles.VISITOR_ROLE_NAME)
    @Transactional
    @PutMapping()
    public Object setProfile(@RequestBody ProfilePo profilePo) {
        try {
            Assert.notNull(profilePo.getId(), "profile.id(which equals to user.id) cannot be null");
        } catch (IllegalArgumentException | NullPointerException e) {
            return R.error(e.getMessage());
        }
        // 为空的字段不会被更新!!!
        profileMapper.updateById(profilePo);
        return R.ok(profileMapper.selectById(profilePo));
    }

    @Secured(Roles.VISITOR_ROLE_NAME)
    @Transactional
    @PostMapping()
    public Object newProfile(@RequestBody ProfilePo profilePo) {
        try {
            Assert.notNull(profilePo.getId(), "profile.id(which equals to user.id) cannot be null");
        } catch (IllegalArgumentException | NullPointerException e) {
            return R.error(e.getMessage());
        }
        profileMapper.insert(profilePo);
        return R.ok(profileMapper.selectById(profilePo));
    }

    @GetMapping("find")
    public ResponseEntity<List<ProfilePo>> findUser(@RequestParam String keyword, @RequestParam Integer page ,@RequestParam Integer limit) {
        return ResponseEntity.ok(profileMapper.selectPage(new Page<ProfilePo>(page, limit),
                Wrappers.lambdaQuery(ProfilePo.class).like(ProfilePo::getNickname, keyword)).getRecords());
    }

}
