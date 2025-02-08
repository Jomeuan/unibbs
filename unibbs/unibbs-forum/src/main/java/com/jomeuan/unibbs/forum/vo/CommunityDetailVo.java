package com.jomeuan.unibbs.forum.vo;

import java.util.List;

import com.jomeuan.unibbs.forum.entity.CommunityContentPo;
import com.jomeuan.unibbs.forum.entity.ModeratorPo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommunityDetailVo {
    private CommunityContentPo communityContent;
    private List<ModeratorVo> moderators;
    private List<PostVo> posts;
    Integer page;
    Integer limit;
}
