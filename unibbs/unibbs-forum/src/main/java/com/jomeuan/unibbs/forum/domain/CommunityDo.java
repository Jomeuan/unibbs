package com.jomeuan.unibbs.forum.domain;

import java.util.List;

import org.springframework.util.Assert;

import com.jomeuan.unibbs.forum.entity.ActionPo;
import com.jomeuan.unibbs.forum.entity.CommunityContentPo;
import com.jomeuan.unibbs.forum.vo.ModeratorVo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommunityDo {
    private ActionPo action;
    private CommunityContentPo communityContent;
    private List<ModeratorVo> moderators;

}
