package com.jomeuan.unibbs.domain;

import java.util.List;

import com.jomeuan.unibbs.entity.ActionPo;
import com.jomeuan.unibbs.entity.CommunityContentPo;
import com.jomeuan.unibbs.vo.ModeratorVo;

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
